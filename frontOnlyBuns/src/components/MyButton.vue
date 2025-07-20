<template>
  <div class="access-panel">
    <button @click="navigateToLogin" class="access-btn login-style">Log in</button>
    <button @click="navigateToSignup" class="access-btn signup-style">Sign in</button>
  </div>
</template>

<script>
export default {
  methods: {
    navigateToLogin() {
      console.log("Redirecting to Login Page");
      this.$router.push({ name: 'Login' });
    },
    navigateToSignup() {
      console.log("Redirecting to Registration Page");
      this.$router.push({ name: 'Register' });
    }
  }
};
</script>

<style scoped>
.access-panel {
  position: fixed;
  display: flex;
  gap: 12px;
  top: 30px;
  right: 30px;
  flex-wrap: wrap;
}

.access-btn {
  padding: 14px;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: 0.3s ease;
  flex: 1;
  min-width: 95px;
  max-width: 48%;
  box-sizing: border-box;
}

/* Login button style */
.login-style {
  background-color: #ffa94d;
  color: #fff;
  border: none;
}

.login-style:hover {
  background-color: #cc4b00;
}

/* Register button style */
.signup-style {
  background-color: #ffffff;
  color: #ffa94d;
  border: 2px solid #ffa94d;
}

.signup-style:hover {
  background-color: #ffe6c1;
}

/* Decorative bunny image style */
.rabbit-gif {
  position: fixed;
  top: 20px;
  left: 20px;
  width: 150px;
  background-color: #ffd1a9;
  border-radius: 15px;
  padding: 5px;
  z-index: 10;
}
</style>
