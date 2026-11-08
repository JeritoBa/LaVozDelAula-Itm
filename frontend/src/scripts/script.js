const apiOrigin = new URL(window.location.origin);
if (apiOrigin.hostname.includes("-5500.")) {
	apiOrigin.hostname = apiOrigin.hostname.replace("-5500.", "-8080.");
} else if (apiOrigin.protocol === "file:") {
	apiOrigin.protocol = "http:";
	apiOrigin.hostname = "localhost";
	apiOrigin.port = "8080";
} else if (["localhost", "127.0.0.1"].includes(apiOrigin.hostname)) {
	apiOrigin.protocol = "http:";
	apiOrigin.port = "8080";
}

const configuredReviewUrl = window.LVA_API_URL || new URL("/api/resenas", apiOrigin).toString();
const API_URL = new URL(configuredReviewUrl, apiOrigin).toString();
const API_ORIGIN = new URL(API_URL).origin;
const state = {
	reviews: [],
	classes: [],
	teachers: new Map(),
	subjects: new Map(),
	filter: "all",
	query: "",
	sort: "recent",
	source: "loading",
	loading: false
};

const reviewList = document.querySelector("#review-list");
const reviewDialog = document.querySelector("#review-dialog");
const reviewForm = document.querySelector("#review-form");
const toast = document.querySelector("#toast");
let toastTimeout;

function apiUrl(path) {
	return new URL(path, API_ORIGIN).toString();
}

async function requestJson(url, options = {}) {
	const response = await fetch(url, {
		...options,
		headers: { Accept: "application/json", ...options.headers }
	});
	if (!response.ok) {
		let message = `La API respondió ${response.status}.`;
		try {
			const error = await response.json();
			if (error.message) message = error.message;
		} catch {
			message = response.statusText || message;
		}
		throw new Error(message);
	}
	return response.status === 204 ? null : response.json();
}

function setConnectionStatus(source) {
	state.source = source;
	const label = document.querySelector("#connection-status");
	const dot = document.querySelector("#connection-dot");
	const retry = document.querySelector("#retry-api");
	label.textContent = source === "online"
		? "Conectado a la base de datos"
		: source === "loading" ? "Cargando experiencias…" : "No fue posible conectar con la base de datos";
	dot.classList.toggle("is-online", source === "online");
	dot.classList.toggle("is-error", source === "error");
	retry.hidden = source !== "error";
}

function classOptionLabel(classInfo) {
	const teacher = state.teachers.get(classInfo.idDocente);
	const teacherName = teacher?.nombre || `Docente #${classInfo.idDocente}`;
	const schedule = classInfo.jornada ? ` · ${classInfo.jornada}` : "";
	return `${teacherName} · ${classInfo.codigo}${schedule}`;
}

function populateClassOptions(selectedClassId = "") {
	const select = document.querySelector("#class-id");
	select.replaceChildren();
	const prompt = document.createElement("option");
	prompt.value = "";
	prompt.textContent = state.classes.length ? "Selecciona el docente y su grupo" : "No hay grupos registrados";
	select.append(prompt);
	for (const classInfo of state.classes) {
		const option = document.createElement("option");
		option.value = classInfo.idClase;
		option.textContent = classOptionLabel(classInfo);
		select.append(option);
	}
	select.value = String(selectedClassId || "");
	document.querySelector("#submit-review").disabled = state.classes.length === 0;
}

function normalizeReview(review) {
	const classInfo = state.classes.find(item => Number(item.idClase) === Number(review.idClase));
	const subjectId = review.idMateria || classInfo?.idMateria;
	const subject = state.subjects.get(Number(subjectId));
	const teacher = state.teachers.get(Number(classInfo?.idDocente));
	return {
		...review,
		calificacionGeneral: Number(review.calificacionGeneral) || 0,
		metodologiaEnsenansa: review.metodologiaEnsenansa == null ? null : Number(review.metodologiaEnsenansa),
		nivelExigencia: review.nivelExigencia == null ? null : Number(review.nivelExigencia),
		anonima: Boolean(review.anonima),
		targetTitle: subject?.nombreMateria || (subjectId ? `Asignatura #${subjectId}` : "Asignatura no disponible"),
		targetCode: classInfo?.codigo || (review.idClase ? `Grupo #${review.idClase}` : "Asignatura"),
		teacherName: teacher?.nombre || (classInfo ? `Docente #${classInfo.idDocente}` : "Docente no disponible")
	};
}

async function loadReviews() {
	if (state.loading) return;
	state.loading = true;
	setConnectionStatus("loading");
	reviewList.setAttribute("aria-busy", "true");
	try {
		const [reviews, classes, teachers, subjects] = await Promise.all([
			requestJson(API_URL),
			requestJson(apiUrl("/api/clases")),
			requestJson(apiUrl("/api/docentes")),
			requestJson(apiUrl("/api/materias"))
		]);
		if (![reviews, classes, teachers, subjects].every(Array.isArray)) {
			throw new Error("La API devolvió datos con un formato inesperado.");
		}
		state.classes = classes;
		state.teachers = new Map(teachers.map(teacher => [Number(teacher.idDocente), teacher]));
		state.subjects = new Map(subjects.map(subject => [Number(subject.idMateria), subject]));
		state.reviews = reviews.map(normalizeReview);
		populateClassOptions(document.querySelector("#class-id").value);
		setConnectionStatus("online");
	} catch (error) {
		state.reviews = [];
		state.classes = [];
		state.teachers.clear();
		state.subjects.clear();
		populateClassOptions();
		setConnectionStatus("error");
		console.error("No se pudieron cargar datos desde la API", error);
	} finally {
		state.loading = false;
		render();
	}
}

function escapeHTML(value = "") {
	return String(value).replace(/[&<>"']/g, character => ({
		"&": "&amp;", "<": "&lt;", ">": "&gt;", "\"": "&quot;", "'": "&#39;"
	})[character]);
}

function formatDate(value) {
	if (!value) return "Fecha no disponible";
	const date = new Date(value);
	if (Number.isNaN(date.getTime())) return "Fecha no disponible";
	return new Intl.DateTimeFormat("es-CO", { day: "numeric", month: "short", year: "numeric" }).format(date);
}

function visibleReviews() {
	const query = state.query.trim().toLocaleLowerCase("es");
	return state.reviews.filter(review => {
		const matchesQuery = !query || [review.targetTitle, review.targetCode, review.teacherName, review.comentario]
			.some(value => String(value || "").toLocaleLowerCase("es").includes(query));
		const matchesFilter = state.filter === "all"
			|| (state.filter === "excellent" && review.calificacionGeneral >= 4)
			|| (state.filter === "needs-context" && Boolean(review.comentario?.trim()));
		return matchesQuery && matchesFilter;
	}).sort((first, second) => {
		if (state.sort === "highest") return second.calificacionGeneral - first.calificacionGeneral;
		if (state.sort === "lowest") return first.calificacionGeneral - second.calificacionGeneral;
		return new Date(second.fechaCreacion || 0) - new Date(first.fechaCreacion || 0);
	});
}

function reviewCard(review, index) {
	const studentId = document.querySelector("#student-id").value;
	const editable = review.idClase != null && studentId && String(review.idEstudiante) === String(studentId);
	const comment = review.comentario?.trim();
	const method = review.metodologiaEnsenansa == null ? "Sin calificación" : `${review.metodologiaEnsenansa.toFixed(1)} metodología`;
	const demand = review.nivelExigencia == null ? "Sin calificación" : `${review.nivelExigencia.toFixed(1)} exigencia`;
	const author = review.anonima ? "Estudiante anónimo" : `Estudiante ${escapeHTML(review.idEstudiante)}`;
	return `<article class="review-card" style="animation-delay:${Math.min(index * 55, 220)}ms">
		<div class="review-top">
			<div><p class="review-course">${escapeHTML(review.targetCode)} · ${escapeHTML(review.targetTitle)}</p><h3>${escapeHTML(review.targetTitle)}</h3><p class="review-teacher">${escapeHTML(review.teacherName)}</p></div>
			<div class="review-score" aria-label="Calificación ${review.calificacionGeneral.toFixed(1)} de 5"><strong>${review.calificacionGeneral.toFixed(1)}</strong><span aria-hidden="true">★</span></div>
		</div>
		<p class="review-comment${comment ? "" : " is-empty"}">${comment ? escapeHTML(comment) : "Esta experiencia no incluye un comentario."}</p>
		<div class="review-bottom"><span class="review-author">${author} · ${method} · ${demand}</span><span class="review-date">${formatDate(review.fechaCreacion)}</span>${editable ? `<button class="review-edit" type="button" data-edit-review="${escapeHTML(review.idResena)}">Editar</button>` : ""}</div>
	</article>`;
}

function setAverage(id, meterId, reviews, property) {
	const values = reviews.map(review => review[property]).filter(value => Number.isFinite(value));
	const average = values.length ? values.reduce((sum, value) => sum + value, 0) / values.length : null;
	document.querySelector(`#${id}`).textContent = average == null ? "—" : average.toFixed(1);
	document.querySelector(`#${meterId}`).style.width = average == null ? "0%" : `${Math.max(0, Math.min(100, average * 20))}%`;
}

function render() {
	const reviews = visibleReviews();
	reviewList.innerHTML = reviews.length
		? reviews.map(reviewCard).join("")
		: state.source === "error"
			? `<div class="empty-state"><strong>No se pudieron cargar las reseñas.</strong>Verifica que el backend esté en ejecución y vuelve a intentarlo.</div>`
			: `<div class="empty-state"><strong>Aún no hay voces por aquí.</strong>Prueba otra búsqueda o comparte la primera experiencia.</div>`;
	reviewList.setAttribute("aria-busy", "false");
	document.querySelector("#result-count").textContent = `${reviews.length} ${reviews.length === 1 ? "experiencia" : "experiencias"}`;
	document.querySelector("#total-count").textContent = state.reviews.length;
	setAverage("average-general", "meter-general", state.reviews, "calificacionGeneral");
	setAverage("average-method", "meter-method", state.reviews, "metodologiaEnsenansa");
	setAverage("average-demand", "meter-demand", state.reviews, "nivelExigencia");
}

function showToast(message) {
	toast.textContent = message;
	toast.classList.add("is-visible");
	clearTimeout(toastTimeout);
	toastTimeout = setTimeout(() => toast.classList.remove("is-visible"), 3800);
}

function openReview(review = null) {
	reviewForm.reset();
	document.querySelector("#review-id").value = review ? review.idResena : "";
	document.querySelector("#dialog-title").textContent = review ? "Edita tu experiencia." : "Comparte tu experiencia.";
	document.querySelector("#submit-review").textContent = review ? "Guardar cambios ↗" : "Publicar reseña ↗";
	document.querySelector("#student-id").value = review?.idEstudiante || "";
	populateClassOptions(review?.idClase || state.classes[0]?.idClase || "");
	document.querySelector("#rating-general").value = review?.calificacionGeneral ?? 4.5;
	document.querySelector("#rating-method").value = review?.metodologiaEnsenansa ?? 4;
	document.querySelector("#rating-demand").value = review?.nivelExigencia ?? 3.5;
	document.querySelector("#review-comment").value = review?.comentario || "";
	document.querySelector("#anonymous-choice").checked = review ? review.anonima : true;
	const error = document.querySelector("#form-error");
	error.textContent = state.classes.length ? "" : "No hay grupos registrados en la base de datos.";
	error.hidden = state.classes.length > 0;
	reviewDialog.showModal();
	updateRangeOutputs();
}

function updateRangeOutputs() {
	document.querySelectorAll(".range-field input").forEach(input => {
		input.parentElement.querySelector("output").value = Number(input.value).toFixed(1);
	});
}

function getFormPayload() {
	const formData = new FormData(reviewForm);
	return {
		calificacionGeneral: Number(formData.get("calificacionGeneral")),
		metodologiaEnsenansa: Number(formData.get("metodologiaEnsenansa")),
		nivelExigencia: Number(formData.get("nivelExigencia")),
		comentario: String(formData.get("comentario") || "").trim(),
		anonima: formData.get("anonima") === "on",
		idEstudiante: Number(formData.get("idEstudiante")),
		idClase: Number(formData.get("idClase")),
		idMateria: null
	};
}

async function submitReview(event) {
	event.preventDefault();
	if (!reviewForm.reportValidity()) return;
	const payload = getFormPayload();
	if (!Number.isInteger(payload.idEstudiante) || payload.idEstudiante < 1) {
		const error = document.querySelector("#form-error");
		error.textContent = "Ingresa un ID de estudiante válido.";
		error.hidden = false;
		return;
	}
	if (!Number.isInteger(payload.idClase) || payload.idClase < 1) {
		const error = document.querySelector("#form-error");
		error.textContent = "Selecciona el docente y grupo que quieres reseñar.";
		error.hidden = false;
		return;
	}

	const existingId = document.querySelector("#review-id").value;
	const submitButton = document.querySelector("#submit-review");
	submitButton.disabled = true;
	submitButton.textContent = "Guardando…";
	try {
		const url = existingId ? `${API_URL}/${encodeURIComponent(existingId)}` : API_URL;
		const saved = await requestJson(url, {
			method: existingId ? "PUT" : "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		});
		const normalized = normalizeReview(saved);
		if (existingId) {
			state.reviews = state.reviews.map(review => String(review.idResena) === existingId ? normalized : review);
		} else {
			state.reviews.unshift(normalized);
		}
		setConnectionStatus("online");
		reviewDialog.close();
		render();
		showToast(existingId ? "Tu reseña se actualizó en la base de datos." : "Tu reseña se guardó en la base de datos.");
	} catch (error) {
		const formError = document.querySelector("#form-error");
		formError.textContent = error.message || "No fue posible guardar la reseña.";
		formError.hidden = false;
		setConnectionStatus("error");
	} finally {
		submitButton.disabled = state.classes.length === 0;
		submitButton.textContent = existingId ? "Guardar cambios ↗" : "Publicar reseña ↗";
	}
}

document.querySelectorAll("[data-open-review]").forEach(button => button.addEventListener("click", () => openReview()));
document.querySelectorAll("[data-close-dialog]").forEach(button => button.addEventListener("click", () => reviewDialog.close()));
document.querySelectorAll("[data-filter]").forEach(button => button.addEventListener("click", () => {
	state.filter = button.dataset.filter;
	document.querySelectorAll("[data-filter]").forEach(chip => {
		const active = chip === button;
		chip.classList.toggle("is-active", active);
		chip.setAttribute("aria-pressed", String(active));
	});
	render();
}));
document.querySelector("#search-form").addEventListener("submit", event => {
	event.preventDefault();
	state.query = document.querySelector("#search-input").value;
	render();
});
document.querySelector("#search-input").addEventListener("input", event => {
	state.query = event.target.value;
	render();
});
document.querySelector("#sort-select").addEventListener("change", event => {
	state.sort = event.target.value;
	render();
});
document.querySelector("#student-id").addEventListener("input", render);
document.querySelector("#retry-api").addEventListener("click", loadReviews);
document.querySelectorAll(".range-field input").forEach(input => input.addEventListener("input", updateRangeOutputs));
reviewList.addEventListener("click", event => {
	const button = event.target.closest("[data-edit-review]");
	if (!button) return;
	const review = state.reviews.find(item => String(item.idResena) === button.dataset.editReview);
	if (review) openReview(review);
});
reviewForm.addEventListener("submit", submitReview);

loadReviews();
window.setInterval(() => {
	if (!document.hidden) loadReviews();
}, 30000);
