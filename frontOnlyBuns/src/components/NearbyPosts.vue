<template>
  <div id="nearby-posts" style="height: 100vh;">
    <div id="map" style="height: 100%;"></div>
  </div>
</template>

<script>
import axios from "axios";
import L from "leaflet";
import { jwtDecode } from "jwt-decode";

export default {
  name: "NearbyPosts",
  data() {
    return {
      map: null, // Map reference
      token: null, // Token reference
    };
  },
  mounted() {
    this.initializeMap();
    
    // Refresh map data every 5 seconds
    setInterval(this.refreshMapData, 500000);
  },
  methods: {
    // Initialize the map
    initializeMap() {
      this.map = L.map("map", {
        center: [45.2671, 19.8335], // Default center to Novi Sad
        zoom: 13, // Default zoom level
      });

      // Add OpenStreetMap layer
      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors",
      }).addTo(this.map);

      this.token = this.$route.query.token;
      if (this.token) {
        console.log("Received token:", this.token);
      } else {
        console.error("Token is missing. Redirecting to login.");
        this.$router.push("/login");
      }

      // Decode the token
      const decoded = jwtDecode(this.token);
      const username = decoded.sub;
      console.log("Username found:", username);

      // Fetch user's location
      this.fetchUserLocation(username);
      // Fetch nearby posts
     // this.fetchNearbyPosts(); ovo je rekao da obrisem
      // Fetch care locations
     // this.fetchCareLocations();
    },

    // Fetch user's location
    fetchUserLocation(username) {
      axios
        .get(`http://localhost:8080/users/location/${username}`)
        .then((response) => {
          const { latitude, longitude } = response.data;
          // Check if map is initialized before setting view
          if (this.map) {
            this.map.setView([latitude, longitude], 13); // Center on user's location

            var houseIcon = L.icon({
              iconUrl:
                "https://static.thenounproject.com/png/3574480-200.png", // Public house icon URL
              iconSize: [32, 32], // Adjust the size of the icon
              iconAnchor: [16, 32], // Anchor the icon at the bottom center
              popupAnchor: [0, -32], // Adjust the popup position
            });

            L.marker([latitude, longitude], { icon: houseIcon })
              .addTo(this.map)
              .bindPopup("<b>" + username + "</b>")
              .openPopup();
          }
          this.fetchNearbyPosts(latitude, longitude);

        })
        .catch((error) => {
          console.error("Error fetching user location:", error.message);
          // Fall back to Novi Sad if there's an error
          if (this.map) {
            this.map.setView([45.2671, 19.8335], 13); // Fallback to Novi Sad
          }
        });
    },

    // Fetch nearby posts
    // fetchNearbyPosts() {
    //   const postIcon = L.icon({
    //     iconUrl: "https://cdn-icons-png.flaticon.com/512/684/684908.png",
    //     iconSize: [32, 32],
    //     iconAnchor: [16, 32],
    //     popupAnchor: [0, -32],
    //   });

    //   axios
    //     .get("http://localhost:8080/posts/locations")
    //     .then((response) => {
    //        console.log("Received posts:", response.data); // dodaatoooooooooo
    //       response.data.forEach((post) => {
    //         // Only attempt to add markers if the map is available
    //         if (this.map) {
    //           L.marker([post.latitude, post.longitude], { icon: postIcon })
    //             .addTo(this.map)
    //             .bindPopup(`
    //               <div style="text-align: center;">
    //                 <b>${post.description}</b><br>
    //                 <img src="${post.imageUrl}" alt="Image" style="width: 100px; height: 100px; object-fit: cover;">
    //               </div>
    //             `);
    //         }
    //       });
    //     })
    //     .catch((error) => console.error("Error fetching posts:", error));
    // },

    fetchNearbyPosts(latitude, longitude) {
      console.log("Pozivam nearby sa: lat=", latitude, "lon=", longitude);

  const postIcon = L.icon({
    iconUrl: "https://cdn-icons-png.flaticon.com/512/684/684908.png",
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
  });

  axios
    .get(`http://localhost:8080/posts/nearby?lat=${latitude}&lon=${longitude}`)
    .then((response) => {
      console.log("Nearby posts:", response.data);
      response.data.forEach((post) => {
        if (this.map) {
          L.marker([post.latitude, post.longitude], { icon: postIcon })
            .addTo(this.map)
            .bindPopup(`
              <div style="text-align: center;">
                <b>${post.description}</b><br>
                <img src="${post.imageUrl}" alt="Image" style="width: 100px; height: 100px; object-fit: cover;">
              </div>
            `);
        }
      });
    })
    .catch((error) => console.error("Error fetching nearby posts:", error));
},







    // Fetch care locations
    // fetchCareLocations() {
    //   const careLocationIcon = L.icon({
    //     iconUrl: "https://cdn-icons-png.flaticon.com/512/616/616557.png", // Care location icon
    //     iconSize: [32, 32],
    //     iconAnchor: [16, 32],
    //     popupAnchor: [0, -32],
    //   });

    //   axios
    //     .get("http://localhost:8080/care-location")
    //     .then((response) => {
    //       response.data.forEach((location) => {
    //         // Only attempt to add markers if the map is available
    //         if (this.map) {
    //           L.marker([location.latitude, location.longitude], { icon: careLocationIcon })
    //             .addTo(this.map)
    //             .bindPopup(`
    //               <div style="text-align: center;">
    //                 <b>${location.name}</b>
    //               </div>
    //             `);
    //         }
    //       });
    //     })
    //     .catch((error) => console.error("Error fetching care locations:", error));
    // },

    // Refresh map data every 5 seconds
    refreshMapData() {
      if (this.map) {
        // Clear the current markers
        this.map.eachLayer((layer) => {
          if (layer instanceof L.Marker) {
            this.map.removeLayer(layer);
          }
        });

        // Re-fetch user location, posts, and care locations
        const decoded = jwtDecode(this.token);
        const username = decoded.sub;
        console.log("Refreshing map data for:", username);

        this.fetchUserLocation(username);
        this.fetchNearbyPosts();
       // this.fetchCareLocations();
      }
    },
  },
};
</script>

<style scoped>
#map {
  width: 100%;
  height: 100%;
}
</style>
