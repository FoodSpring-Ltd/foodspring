document.addEventListener("DOMContentLoaded", function() {
    const navLinks = document.querySelectorAll(".nav-link");

    navLinks.forEach(link => {
      link.addEventListener("click", function() {
        navLinks.forEach(link => link.classList.remove("active"));
        this.classList.add("active");
      });
    });
 });