<template>
  <div class="fixed-home-wrapper">
    <button @click="redirectHome" class="home-nav-btn">
      <i class="fas fa-home"></i>
    </button>
  </div>
</template>

<script>
export default {
  methods: {
    redirectHome() {
      const userToken = sessionStorage.getItem('token');
      if (userToken) {
        window.location.href = `http://localhost:8081?token=${userToken}`;
      } else {
        window.location.href = 'http://localhost:8081';
      }
    }
  }
};
</script>

<style scoped>
.fixed-home-wrapper {
  position: fixed;
  top: 12px;
  right: 12px;
  z-index: 999;
}

.home-nav-btn {
  background-color: #f0f0f0;
  padding: 9px 18px;
  border: none;
  border-radius: 6px;
  font-size: 15px;
  cursor: pointer;
}

.home-nav-btn:hover {
  background-color: #dcdcdc;
}
</style>
