/**
 * Steuert den Dark/Light-Mode-Umschalter.
 * Speichert die Auswahl im localStorage, damit sie
 * bei einem erneuten Seitenbesuch erhalten bleibt.
 */
document.addEventListener("DOMContentLoaded", function () {
    const toggleButton = document.getElementById("theme-toggle");
    const body = document.body;

    if (localStorage.getItem("theme") === "dark") {
        body.classList.add("dark-mode");
        toggleButton.textContent = "Light Mode";
    }

    toggleButton.addEventListener("click", function () {
        body.classList.toggle("dark-mode");
        const isDark = body.classList.contains("dark-mode");
        toggleButton.textContent = isDark ? "Light Mode" : "Dark Mode";
        localStorage.setItem("theme", isDark ? "dark" : "light");
    });
});