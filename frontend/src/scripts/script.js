const API_URL = window.LVA_API_URL || "http://localhost:8080/api/resenas";
const STORAGE_KEY = "la-voz-del-aula-resenas-v1";
const CLASS_CATALOG = {
	1: { title: "Bases de Datos", teacher: "Carlos Andrés Gómez", code: "BD-01" }
};
const SUBJECT_CATALOG = { 1: "Bases de Datos" };

const sampleReviews = [
	{
		idResena: 1,
		calificacionGeneral: 4.5,
		metodologiaEnsenansa: 4,
		nivelExigencia: 3.5,
		comentario: "Excelente clase, el docente explica muy bien los conceptos de bases de datos.",
		fechaCreacion: "2026-09-25T14:30:00",
		anonima: false,
		idEstudiante: 1,
		idClase: 1,
		idMateria: null
	}
];

const state = { reviews: [], filter: "all", query: "", sort: "recent", source: "loading" };
const reviewList = document.querySelector("#review-list");
const reviewDialog = document.querySelector("#review-dialog");
const reviewForm = document.querySelector("#review-form");
const toast = document.querySelector("#toast");
let toastTimeout;

function readLocalReviews() {
	try {
		const stored = JSON.parse(localStorage.getItem(STORAGE_KEY) || "[]");
		return Array.isArray(stored) ? stored : [];
	} catch {
		return [];
	}
}

function storeLocalReviews(reviews) {
	localStorage.setItem(STORAGE_KEY, JSON.stringify(reviews));
}

function normalizeReview(review) {
	const classInfo = CLASS_CATALOG[review.idClase];
	const subject = review.materiaNombre || SUBJECT_CATALOG[review.idMateria];
	return {
		...review,
		idResena: review.idResena ?? `local-${Date.now()}`,
		calificacionGeneral: Number(review.calificacionGeneral) || 0,
		metodologiaEnsenansa: review.metodologiaEnsenansa == null ? null : Number(review.metodologiaEnsenansa),
		nivelExigencia: review.nivelExigencia == null ? null : Number(review.nivelExigencia),
		anonima: Boolean(review.anonima),
		targetTitle: review.materiaNombre || classInfo?.title || subject || (review.idClase ? `Clase #${review.idClase}` : `Materia #${review.idMateria}`),
		targetCode: classInfo?.code || (review.idMateria || review.materiaNombre ? "Materia" : "Clase"),
		teacherName: classInfo?.teacher || "Docente de la asignatura",
		isLocal: Boolean(review.isLocal)
	};
}

function setConnectionStatus(source) {
	state.source = source;
	const label = document.querySelector("#connection-status");
	const dot = document.querySelector("#connection-dot");
	const online = source === "online";
	label.textContent = online ? "Conectado a la API" : source === "loading" ? "Cargando experiencias…" : "Modo local · API no disponible";
	dot.classList.toggle("is-online", online);
	dot.classList.toggle("is-local", source === "local");
}

async function loadReviews() {
	const localReviews = readLocalReviews();
	try {
		const response = await fetch(API_URL, { headers: { Accept: "application/json" } });
		if (!response.ok) throw new Error(`API ${response.status}`);
		const data = await response.json();
		if (!Array.isArray(data)) throw new Error("La API no devolvió una lista de reseñas");
		const serverReviews = data.map(normalizeReview);
		const knownIds = new Set(serverReviews.map(review => String(review.idResena)));
		const pendingReviews = localReviews.map(normalizeReview).filter(review => !knownIds.has(String(review.idResena)));
		state.reviews = [...pendingReviews, ...serverReviews];
		setConnectionStatus("online");
	} catch {
		state.reviews = (localReviews.length ? localReviews : sampleReviews.map(review => ({ ...review, isLocal: true }))).map(normalizeReview);
		setConnectionStatus("local");
	}
	render();
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
	const editable = String(review.idEstudiante) === String(document.querySelector("#student-id").value);
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
	document.querySelector("#submit-review").innerHTML = review ? "Guardar cambios <span aria-hidden=\"true\">↗</span>" : "Publicar reseña <span aria-hidden=\"true\">↗</span>";
	document.querySelector("#student-id").value = review?.idEstudiante || 1;
	document.querySelector("#target-type").value = review?.materiaNombre || review?.idMateria ? "subject" : "class";
	document.querySelector("#target-id").value = review?.idMateria || review?.idClase || 1;
	document.querySelector("#subject-name").value = review?.materiaNombre || SUBJECT_CATALOG[review?.idMateria] || "";
	document.querySelector("#rating-general").value = review?.calificacionGeneral ?? 4.5;
	document.querySelector("#rating-method").value = review?.metodologiaEnsenansa ?? 4;
	document.querySelector("#rating-demand").value = review?.nivelExigencia ?? 3.5;
	document.querySelector("#review-comment").value = review?.comentario || "";
	document.querySelector("#anonymous-choice").checked = review ? review.anonima : true;
	document.querySelector("#form-error").hidden = true;
	updateTargetLabel();
	updateRangeOutputs();
	reviewDialog.showModal();
}

function updateTargetLabel() {
	const isClass = document.querySelector("#target-type").value === "class";
	document.querySelector("#target-label").hidden = !isClass;
	document.querySelector("#target-id").required = isClass;
	document.querySelector("#subject-label").hidden = isClass;
	document.querySelector("#subject-name").required = !isClass;
	document.querySelector("#target-label").firstChild.textContent = isClass ? "Clase" : "Materia";
	document.querySelector("#target-id").innerHTML = isClass
		? '<option value="1">BD-01 · Bases de Datos · Carlos Andrés Gómez</option>'
		: '<option value="1">Bases de Datos</option>';
}

function updateRangeOutputs() {
	document.querySelectorAll(".range-field input").forEach(input => {
		input.parentElement.querySelector("output").value = Number(input.value).toFixed(1);
	});
}

function getFormPayload() {
	const formData = new FormData(reviewForm);
	const targetType = formData.get("targetType");
	const targetId = Number(formData.get("targetId"));
	const materiaNombre = String(formData.get("materiaNombre") || "").trim();
	const catalogEntry = Object.entries(SUBJECT_CATALOG).find(([, name]) => name.toLocaleLowerCase("es") === materiaNombre.toLocaleLowerCase("es"));
	return {
		calificacionGeneral: Number(formData.get("calificacionGeneral")),
		metodologiaEnsenansa: Number(formData.get("metodologiaEnsenansa")),
		nivelExigencia: Number(formData.get("nivelExigencia")),
		comentario: String(formData.get("comentario")).trim(),
		anonima: formData.get("anonima") === "on",
		idEstudiante: Number(formData.get("idEstudiante")),
		idClase: targetType === "class" ? targetId : null,
		idMateria: targetType === "subject" && catalogEntry ? Number(catalogEntry[0]) : null,
		materiaNombre: targetType === "subject" ? materiaNombre : ""
	};
}

async function submitReview(event) {
	event.preventDefault();
	if (!reviewForm.reportValidity()) return;
	const payload = getFormPayload();
	if (!Number.isInteger(payload.idEstudiante) || payload.idEstudiante < 1) {
		document.querySelector("#form-error").textContent = "Ingresa un ID de estudiante válido.";
		document.querySelector("#form-error").hidden = false;
		return;
	}

	const existingId = document.querySelector("#review-id").value;
	const existingReview = state.reviews.find(review => String(review.idResena) === existingId);
	const customSubject = Boolean(payload.materiaNombre) && payload.idMateria == null;
	if (customSubject && existingId && existingReview && !existingReview.isLocal) {
		document.querySelector("#form-error").textContent = "El backend no permite cambiar una reseña existente a una materia nueva.";
		document.querySelector("#form-error").hidden = false;
		return;
	}
	const submitButton = document.querySelector("#submit-review");
	submitButton.disabled = true;
	submitButton.textContent = "Guardando…";
	try {
		if (customSubject) {
			const localReview = normalizeReview({
				...(existingReview || {}), ...payload,
				idResena: existingId || `local-${Date.now()}`,
				fechaCreacion: existingReview?.fechaCreacion || new Date().toISOString(),
				isLocal: true
			});
			if (existingId) state.reviews = state.reviews.map(review => String(review.idResena) === existingId ? localReview : review);
			else state.reviews.unshift(localReview);
			storeLocalReviews(state.reviews.filter(review => review.isLocal));
			reviewDialog.close();
			render();
			showToast("Guardada en este navegador; las materias nuevas aún no están registradas en el backend.");
			return;
		}
		const apiPayload = { ...payload };
		delete apiPayload.materiaNombre;
		if (existingId && existingReview && !existingReview.isLocal) {
			const response = await fetch(`${API_URL}/${encodeURIComponent(existingId)}`, {
				method: "PUT", headers: { "Content-Type": "application/json", Accept: "application/json" }, body: JSON.stringify(apiPayload)
			});
			if (!response.ok) throw new Error(`API ${response.status}`);
			const updated = await response.json();
			state.reviews = state.reviews.map(review => String(review.idResena) === existingId ? normalizeReview(updated) : review);
			setConnectionStatus("online");
		} else if (!existingId) {
			const response = await fetch(API_URL, {
				method: "POST", headers: { "Content-Type": "application/json", Accept: "application/json" }, body: JSON.stringify(apiPayload)
			});
			if (!response.ok) throw new Error(`API ${response.status}`);
			const created = await response.json();
			state.reviews.unshift(normalizeReview(created));
			setConnectionStatus("online");
		} else {
			const updated = normalizeReview({ ...existingReview, ...payload, isLocal: true });
			state.reviews = state.reviews.map(review => String(review.idResena) === existingId ? updated : review);
			storeLocalReviews(state.reviews.filter(review => review.isLocal));
		}
		reviewDialog.close();
		render();
		showToast(existingId ? "Tu reseña se actualizó." : "Tu reseña se publicó.");
	} catch {
		const localReview = normalizeReview({
			...(existingReview || {}), ...payload,
			idResena: existingId || `local-${Date.now()}`,
			fechaCreacion: existingReview?.fechaCreacion || new Date().toISOString(),
			isLocal: true
		});
		if (existingId) state.reviews = state.reviews.map(review => String(review.idResena) === existingId ? localReview : review);
		else state.reviews.unshift(localReview);
		storeLocalReviews(state.reviews.filter(review => review.isLocal));
		setConnectionStatus("local");
		reviewDialog.close();
		render();
		showToast("Guardada en este navegador. Se conectará al backend cuando esté disponible.");
	} finally {
		submitButton.disabled = false;
		submitButton.innerHTML = existingId ? "Guardar cambios <span aria-hidden=\"true\">↗</span>" : "Publicar reseña <span aria-hidden=\"true\">↗</span>";
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
document.querySelector("#target-type").addEventListener("change", updateTargetLabel);
document.querySelectorAll(".range-field input").forEach(input => input.addEventListener("input", updateRangeOutputs));
reviewList.addEventListener("click", event => {
	const button = event.target.closest("[data-edit-review]");
	if (!button) return;
	const review = state.reviews.find(item => String(item.idResena) === button.dataset.editReview);
	if (review) openReview(review);
});
reviewForm.addEventListener("submit", submitReview);

loadReviews();
