import { createApp } from 'vue'
import App from './App.vue'
import router from './router'; // Uvezi router
import './assets/styles.css';
import 'leaflet/dist/leaflet.css';



createApp(App)
  .use(router) // Registruj router
  .mount('#app');
