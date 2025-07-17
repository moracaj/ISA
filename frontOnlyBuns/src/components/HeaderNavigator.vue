<template>
  <div class="button-container">
    <img
      src="https://i.imgur.com/hDNX2ug.png"
      class="rabbit-logo"
      alt="Zec"
      :class="{ hidden: isScrolled }"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      isScrolled: false,
    };
  },
  methods: {
    handleScroll() {
      // Toggle 'isScrolled' based on scroll position
      this.isScrolled = window.scrollY > 50;
    },
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeUnmount() {
    window.removeEventListener("scroll", this.handleScroll);
  },
};
</script>

<style>
.button-container {
  position: relative;
}

/* Logo styling */
.rabbit-logo {
  position: fixed; /* Keeps the logo fixed in the viewport */
  top: 20px;       /* Adjusted top positioning */
  left: 20px;      /* Adjusted left positioning */
  width: 150px;     /* Adjusted logo size */
  height: auto;    /* Maintains aspect ratio */
 /* Makes the logo circular */
  padding: 5px;
  z-index: 100;    /* Ensures it appears above other elements */
}

.rabbit-logo.hidden {
  display: none;   /* Hides the logo when scrolled */
}
</style>
