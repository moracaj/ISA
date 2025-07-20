<template>
  <div class="logo-box">
    <img
      class="logo-image"
      alt="Bunny"
      src="https://media-private.canva.com/W85xM/MAGWdPW85xM/1/p.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJWF6QO3UH4PAAJ6Q%2F20241114%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20241114T044416Z&X-Amz-Expires=46141&X-Amz-Signature=2dc6d4b7a6b76d3b08dcbbb6ebc2ea50f88eadc37e1996daae2bd0b73baaff9d&X-Amz-SignedHeaders=host%3Bx-amz-expected-bucket-owner&response-expires=Thu%2C%2014%20Nov%202024%2017%3A33%3A17%20GMT"
    />
    <span class="site-title" v-show="!scrolledDown">OnlyBuns</span>
  </div>
</template>

<script>
export default {
  data() {
    return {
      scrolledDown: false
    };
  },
  methods: {
    onScrollChange() {
      this.scrolledDown = window.scrollY > 50;
    }
  },
  mounted() {
    window.addEventListener('scroll', this.onScrollChange);
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.onScrollChange);
  }
};
</script>

<style scoped>
.logo-box {
  position: relative;
}

.logo-image {
  position: fixed;
  top: 10px;
  left: 10px;
  width: 100px;
  height: auto;
  border-radius: 15px;
  background-color: #ffd1a9;
  padding: 5px;
  z-index: 999;
}

.site-title {
  position: fixed;
  top: 50px;
  left: 120px;
  font-size: 34px;
  font-weight: 700;
  color: #d35400;
  z-index: 999;
}
</style>
