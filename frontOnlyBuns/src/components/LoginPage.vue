 <template>
  <div class="form">
    <h2>{{ isRegister ? 'Registration' : 'Log in' }}</h2>
    <HomeButton />

    <template v-if="isRegister">
      <input type="text" v-model="username" placeholder="Enter your username" />
      <input type="email" v-model="registerEmail" placeholder="Enter your email" />
      <input type="password" v-model="registerPassword" placeholder="Enter your password" />
      <input type="password" v-model="confirmPassword" placeholder="Confirm your password" />
      <input type="text" v-model="firstName" placeholder="Enter your first name" />
      <input type="text" v-model="lastName" placeholder="Enter your last name" />
      <input type="text" v-model="address" placeholder="Enter your address" />
      <button @click="register" class="my-button2">Register</button>
    </template>

    <template v-else>
      <input type="email" v-model="email" placeholder="Enter your email" />
      <input type="password" v-model="password" placeholder="Enter your password" />
      <button @click="login" class="my-button2">Log in</button>
    </template>

    <button @click="toggleForm" class="toggle-button2">
      {{ isRegister ? 'Already have an account? Log in' : 'Don\'t have an account? Register' }}
    </button>
  </div>
</template> 



<script>
import HomeButton from './HomePageButton.vue';

export default {
  components:{
    HomeButton,

  },
  data() {
    return {
      isRegister: false,
      username: "",
      registerEmail: "",
      registerPassword: "",
      confirmPassword: "",
      firstName: "",
      lastName: "",
      address: "",
      email: "",
      password: ""
    };
  },
  methods: {
    toggleForm() {
      this.$router.push({ name: 'Register' });
      
    },  
  
    async login() {
  try {
    const response = await fetch("http://localhost:8080/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email: this.email, password: this.password })
    });

    const data = await response.json();

    if (response.ok) {
      console.log("User role: ", data.userType);

      // Sačuvaj token i tip korisnika u sessionStorage
      sessionStorage.setItem('token', data.token); // Sačuvaj token
      sessionStorage.setItem('userType', data.userType); // Sačuvaj tip korisnika

      // Ako je korisnik admin, preusmeri ga na Admin stranicu
      if (data.userType === "ROLE_ADMIN") {
        this.$router.push({ name: "Admin" });
      } else if (data.userType === "ROLE_REGISTERED") {
        this.$router.push({ name: "Registered" });
      } else {
        alert("Unknown user type.");
      }
    } else {
      alert(data.message || "Error during login!");
    }
  } catch (error) {
    alert("An error occurred: " + error.message);
  }
},
    async register() {
      if (this.registerPassword !== this.confirmPassword) {
        alert("Passwords do not match.");
        return;
      }

      try {
        const response = await fetch("http://localhost:8080/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: this.username,
            email: this.registerEmail,
            password: this.registerPassword,
            firstName: this.firstName,
            lastName: this.lastName,
            address: this.address
          })
        });

        const data = await response.json();

        if (response.ok) {
          alert("Registration successful!");
        } else {
          alert(data.message || "Registration failed. Please try again.");
        }
      } catch (error) {
        alert("An error occurred: " + error.message);
      }
 
    }
  }
};
</script>

<style scoped>
.form {
  margin-top: 30px; /* Prilagođava položaj */
  margin-left: auto; /* Centriranje horizontalno */
  margin-right: auto; /* Centriranje horizontalno */
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #ffecd9; /* Svetlo narandžasta pozadina */
  border-radius: 15px;
  padding: 30px; /* Veći padding za više prostora oko sadržaja */
  width: 600px; /* Širina za bolji raspored */
  height: auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  gap: 20px;
}

h2 {
  font-size: 34px;
  margin-bottom: 20px;
  color: #d35400; /* Tamnija narandžasta za naslov */
}

.input-grid {
  display: grid;
  grid-template-columns: 1fr 1fr; /* Dve kolone */
  gap: 10px; /* Razmak između polja */
  width: 100%; /* Pun široki prikaz */
}

input {
  font-size: 19px;
  height: 50px;
  padding: 10px;
  border: 1px solid #f0a97e; /* Svetlija narandžasta ivica */
  border-radius: 15px;
  background-color: #fff9f0; /* Svetlo narandžasta pozadina unosa */
}

.button-container2 {
  display: flex; /* Centriranje dugmeta */
  justify-content: center;
  margin-top: 20px; /* Prostor iznad dugmeta */
  width: 100%; /* Pun široki prikaz dugmeta */
}

.my-button2 {
  width: 150px;
  height: 40px;
  background-color: #ff9a3b; /* Tamnija narandžasta za dugme */
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: medium;
}

.my-button2:hover {
  background-color: #d35400; /* Još tamnija narandžasta na hover */
}

.toggle-button2 {
  margin-top: 10px;
  background: none;
  border: none;
  font-size: 15px;
  color: #d35400; /* Tamnija narandžasta za tekst dugmeta */
  cursor: pointer;
  text-decoration: underline;
}

</style>
