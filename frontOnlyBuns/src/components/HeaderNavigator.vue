 <template>
  <div class="button-container">
    <img
      src="https://media-private.canva.com/W85xM/MAGWdPW85xM/1/p.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJWF6QO3UH4PAAJ6Q%2F20241114%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20241114T044416Z&X-Amz-Expires=46141&X-Amz-Signature=2dc6d4b7a6b76d3b08dcbbb6ebc2ea50f88eadc37e1996daae2bd0b73baaff9d&X-Amz-SignedHeaders=host%3Bx-amz-expected-bucket-owner&response-expires=Thu%2C%2014%20Nov%202024%2017%3A33%3A17%20GMT"
      class="rabbit-gif"
      alt="Zec"
     
    />
    <span class="brand-name" :class="{ hidden: isScrolled }">OnlyBuns</span>
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

.rabbit-gif {
  position: fixed; /* Drži sliku fiksiranom u prikazu */
  top: 10px;       /* Podešavanje za gornju poziciju */
  left: 10px;      /* Podešavanje za levu poziciju */
  width: 100px;    /* Povećanje širine slike */
  height: auto;    /* Održava proporciju slike */
  border-radius: 15px;
  padding: 5px;
  background-color: #ffd1a9; /* Svetlo narandžasta pozadina za sliku */
  z-index: 100;    /* Osigurava da bude iznad drugih elemenata */
}

.brand-name {
  position: fixed;
  top: 50px; /* Prilagođeno za poravnanje sa slikom */
  left: 120px; /* Prilagođeno za poziciju desno od slike */
  font-size: 35px;
  font-weight: bold;
  color: #d35400; /* Tamnija narandžasta za tekst */
  z-index: 100;
}

.rabbit-gif.hidden {
  display: none;   /* Sakriva sliku kad je skrolovano */
}

</style> 


