document.addEventListener("DOMContentLoaded", function () {
    const toggleBtn = document.getElementById("catalogToggle");
    const menu = document.getElementById("catalogMenu");
  
    toggleBtn.addEventListener("click", function (e) {
      e.stopPropagation();
      menu.style.display = menu.style.display === "block" ? "none" : "block";
    });
  
    document.addEventListener("click", function (e) {
      if (!menu.contains(e.target) && e.target !== toggleBtn) {
        menu.style.display = "none";
      }
    });
  });
  