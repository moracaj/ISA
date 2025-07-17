<!-- <template>
  <div class="button-container">
    <img
      src="https://i.pinimg.com/564x/39/d3/e0/39d3e06ebb09a79f805356b9db516078.jpg"
      class="rabbit-logo"
      alt="Zec"
   
     :class="{ hidden: isScrolled }" 
    />
 <h1 class="logo-text" :class="{ hidden: isScrolled }">OnlyBuns</h1>
   
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
  height: auto;
  max-width: 80px;    /* Maintains aspect ratio */
 /* Makes the logo circular */
  padding: 5px;
  z-index: 100;    /* Ensures it appears above other elements */
}



.rabbit-logo.hidden {
  display: none;   /* Hides the logo when scrolled */
}
</style> -->







<template>
  <div>
    <!-- Logo + OnlyBuns tekst u levom fiksiranom uglu -->
    <div class="logo-container" :class="{ hidden: isScrolled }">
      <img
        src="https://i.pinimg.com/564x/39/d3/e0/39d3e06ebb09a79f805356b9db516078.jpg"
        class="rabbit-logo"
        alt="Zec"
      />
      <h1 class="logo-text">OnlyBuns</h1>
    </div>

   
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
      this.isScrolled = window.scrollY > 50;
    },
    goToLogin() {
      this.$router.push({ name: 'Login' });
    },
    goToRegister() {
      this.$router.push({ name: 'Register' });
    }
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
/* Logo + naziv fiksirani levo */
.logo-container {
  position: fixed;
  top: 20px;
  left: 20px;
  display: flex;
  align-items: center;
  z-index: 100;
}

.rabbit-logo {
  width: 60px;
  height: auto;
  border-radius: 50%;
  padding: 5px;
}

.logo-text {
  margin-left: 12px;
  font-size: 50px;
  font-weight: bold;
  color: rgba(5, 49, 153, 0.726);
  font-family: 'Arial', sans-serif;
}

/* Dugmići fiksirani desno */
.button-container {
  display: flex;
  position: fixed;
  top: 20px;
  right: 30px;
  gap: 10px;
  z-index: 100;
}

.my-button {
  padding: 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 1em;
  min-width: 90px;
}

.my-button.login {
  background-color: #0c335d;
  color: white;
}

.my-button.login:hover {
  background-color: #1c4a80;
}

.my-button.register {
  background-color: white;
  color: #0c335d;
  border: 2px solid #0c335d;
}

.my-button.register:hover {
  background-color: #f0f0f0;
}

.hidden {
  display: none;
}
</style>
