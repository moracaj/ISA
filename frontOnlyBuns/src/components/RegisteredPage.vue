<template>
    <div>
        <SignOutButton />

        <!-- Navigation Links for Authenticated User -->
        <div class="home-navigation">
            <ul>
                <li><router-link to="/following-posts"><i class="fas fa-users"></i> Following Posts</router-link></li>
                <li><router-link to="/trends"><i class="fas fa-chart-line"></i> Trends</router-link></li>
                <li><router-link to="/nearby-posts"><i class="fas fa-map-marker-alt"></i> Nearby Posts</router-link></li>
                <li><router-link to="/chat"><i class="fas fa-comments"></i> Chat</router-link></li>
                <li><router-link to="/profile"><i class="fas fa-user"></i> Profile</router-link></li>
            </ul>

            <!-- <button @click="goHome" class="home-button">Home</button> -->
           <button @click="openAddPostModal" class="add-post-button">Add Post</button>
        </div>

        <!-- Main Content Area -->
        <!-- <main class="home-content">
            <div class="posts-container">
                <div v-for="post in posts" :key="post.id" class="post">
                  <h3>Location:</h3>
                  <p>{{ post.location }}</p>

                    <img :src="post.imageUrl" alt="Post Image" class="post-image" />
                    <div class="post-description">
                        <h3>Description:</h3>
                        <p>{{ post.description }}</p>
                    </div>
                    <h3>Likes:</h3>
                    <p>{{ post.countLikes }}</p>
                    <h3>Comments:</h3>
                    <ul class="comments-list">
                        <li v-for="comment in post.comments" :key="comment.id" class="comment-item">
                            <strong>{{ comment.username }}:</strong> {{ comment.content }}
                        </li>
                    </ul>
                    <div class="post-actions">
                        <button @click="openEditModal(post)" class="edit-button">Edit</button>
                        <button @click="deletePost(post.id)" class="delete-button">Delete</button>
                    </div>
                </div>
            </div>
        </main> -->


  
        <main class="home-content">
  <div class="posts-container">
    <div v-for="post in posts" :key="post.id" class="post">
      <!-- Slika posta -->
      <img :src="post.imageUrl" alt="Post Image" class="post-image" />

      <!-- Opis posta -->
      <div class="post-content">
        <h3>Description:</h3>
        <p class="post-description">{{ post.description }}</p>

        <!-- Broj lajkova i komentara -->
        <div class="post-stats">
          <span class="likes">❤️ {{ post.countLikes }} Likes</span>
          <span class="comments">💬 {{ post.comments.length }} Comments</span>
        </div>

        <!-- Lista komentara -->
        <h3>Comments:</h3>
        <ul class="comments-list">
          <li v-for="comment in post.comments" :key="comment.id" class="comment-item">
            <strong>{{ comment.username }}:</strong> {{ comment.content }}
          </li>
        </ul>

        <!-- Akcije na postu -->
        <!-- <div class="post-actions">
          <button @click="openEditModal(post)" class="edit-button">Edit</button>
          <button @click="deletePost(post.id)" class="delete-button">Delete</button>
        </div> -->
      </div>
    </div>
  </div>
</main>






        <!-- Modal for Editing Post -->
          <!-- <div v-if="showEditModal" class="modal">
            <div class="modal-content">
                <h2>Edit Post</h2>
                <label for="imageUrl">Image URL:</label>
                <input v-model="editData.imageUrl" id="imageUrl" placeholder="Enter new image URL" />

                <label for="description">Description:</label>
                <textarea v-model="editData.description" id="description" placeholder="Enter new description"></textarea>

                <div class="modal-actions">
                    <button @click="confirmEdit" class="confirm-button">Save</button>
                    <button @click="closeEditModal" class="cancel-button">Cancel</button>
                </div>
            </div> 
        </div>-->
    </div>  

  <div v-if="showAddPostModal" class="modal add-post-modal">
    <div class="modal-content">
      <h2>Create New Post</h2>

      <label for="image">Image:</label>
      <input type="file" @change="onFileSelected" id="image" />

      <label for="description">Description:</label>
      <textarea v-model="newPost.description" id="description" placeholder="Enter description"></textarea>

      <label for="location">Location:</label>
      <input v-model="newPost.location" id="location" placeholder="Enter location or select on map" />

      <div id="map" style="height: 300px; margin-top: 20px;"></div>

      <div class="modal-actions">
        <button @click="createPost" class="confirm-button">Save</button>
        <button @click="closeAddPostModal" class="cancel-button">Cancel</button>
      </div>
    </div>
  </div>

</template>

<script>
import SignOutButton from './SignOutButton.vue';
import L from "leaflet";
import "leaflet/dist/leaflet.css";
delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
});

export default {
    components: {
        SignOutButton,
    },
    data() {
        return {
            posts: [],
            isRegistered: false,
            showEditModal: false,
            showAddPostModal: false,
            editData: {
                id: null,
                imageUrl: "",
                description: ""
            },
          newPost: {
            description: '',
            location: '',
            latitude: '',
            longitude: ''
          },
          map: null,
          marker: null,
          selectedFile: null,
        };
    },

    mounted() {
    this.$nextTick(() => {
      if (this.showAddPostModal) {
        this.initializeMap();
      }
    });
  },

    async created() {
        await this.checkRegistered();
        const token = sessionStorage.getItem('token');
        console.log('Token:', token);

        if (this.isRegistered && token) {
            this.fetchPosts();
        } else {
            console.error("User is not registered or token was not found.");
        }
    },
    methods: {
      onFileSelected(event) {
        this.selectedFile = event.target.files[0];
        console.log("File selected:", this.selectedFile);
      },
      async createPost() {
        console.log("Save button clicked - createPost method triggered");
        const token = sessionStorage.getItem('token');
        const formData = new FormData();
        formData.append("imageFile", this.selectedFile);
        formData.append("description", this.newPost.description);
        formData.append("latitude", this.newPost.latitude);
        formData.append("longitude", this.newPost.longitude);
        formData.append("location", this.newPost.location);

        


     


        try {
          const response = await fetch("http://localhost:8080/posts/create", {
            method: "POST",
            headers: {
              "Authorization": `Bearer ${token}`,
              // "Content-Type": "application/json" /////ovo sam dodala
            },
            body: formData,
           //body: JSON.stringify(postDto),
          });

          if (response.ok) {
            const createdPost = await response.json();
            this.posts.unshift(createdPost);
            this.closeAddPostModal();
            console.log("Post successfully created.");
          } else {
            const errorText = await response.text();
            console.error("Error creating post:", errorText);
          }
        } catch (error) {
          console.error("Error creating post:", error.message);
        }
      },

        async checkRegistered() {
            const userType = sessionStorage.getItem('userType');
            this.isRegistered = userType === 'ROLE_REGISTERED';
        },

        async fetchPosts() {
            const token = sessionStorage.getItem('token');
            if (!token) {
                console.error("Token was not found.");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/posts/my-posts", {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                console.log('Response status:', response.status);

                if (!response.ok) {
                    const errorText = await response.text();
                    console.error("Error loading posts:", errorText);
                    return;
                }

                const data = await response.json();
                this.posts = data;
            } catch (error) {
                console.error("An error occurred while loading posts:", error.message);
            }
        },

    
      

        // Function to handle redirection and saving the token
        goHome() {
            const token = sessionStorage.getItem('token'); // Save token if it's not already stored
            if (token) {
                sessionStorage.setItem('token', token);  // Ensure token is saved before redirection
                window.location.href = "http://localhost:8081"; // Redirect to the specified URL
            }
        },

      openAddPostModal() {
        this.showAddPostModal = true;
        this.$nextTick(() => this.initializeMap());
      },
      closeAddPostModal() {
        this.showAddPostModal = false;
        this.clearNewPostForm();
      },

      initializeMap() {
        const mapElement = document.getElementById('map');
        if (!mapElement) {
          console.error("Map element not found.");
          return;
        }

        this.map = L.map('map').setView([44.0165, 21.0059], 7);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          maxZoom: 19,
        }).addTo(this.map);

        this.map.on('click', (e) => {
          this.newPost.latitude = e.latlng.lat;
          this.newPost.longitude = e.latlng.lng;

          if (this.marker) {
            this.marker.setLatLng(e.latlng);
          } else {
            this.marker = L.marker(e.latlng).addTo(this.map);
          }

          this.reverseGeocode(e.latlng.lat, e.latlng.lng);
        });
      },

      async reverseGeocode(lat, lng) {
        try {
          const response = await fetch(`https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json&accept-language=en`);
          const data = await response.json();

          if (data && data.address) {
            const city = data.address.city || data.address.town || data.address.village || "Unknown city";
            const country = data.address.country || "Unknown country";
            this.newPost.location = `${city}, ${country}`;
          } else {
            console.warn("Address not found.");
            this.newPost.location = "Unknown location";
          }
        } catch (error) {
          console.error("Error fetching address:", error);
          this.newPost.location = "Error loading location";
        }
      },

      clearNewPostForm() {
        this.newPost = {
          imageUrl: "",
          description: "",
          latitude: null,
          longitude: null,
          location: ""
        };
        if (this.marker) {
          this.map.removeLayer(this.marker);
        }
      },
    },
};
</script>

<style scoped>
.home-navigation {
    margin-bottom: 20px;
    text-align: center;
}

.home-navigation ul {
    list-style: none;
    padding: 0;
}

.home-navigation ul li {
    display: inline;
    margin-right: 15px;
}

.home-navigation ul li a {
    text-decoration: none;
    font-size: 18px;
    color: #d35400;
}

.home-navigation ul li i {
    margin-right: 8px;
}

/* Style for the home button */
.home-button {
    display: inline-block;
    padding: 10px 20px;
    background-color: #ffb347; /* Green color */
    color: white;
    font-size: 18px;
    text-decoration: none;
    border-radius: 5px;
    margin-top: 20px;
}

.home-button:hover {
    background-color: #ff9a3b;
}

.home-content {
    margin-top: 20px;
}

.posts-container {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    justify-content: space-between;
}

.post {
    flex: 1 1 calc(25% - 20px);
    background-color: #f9f9f9;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.post-image {
    max-width: 100%;
    height: auto;
    border-radius: 5px;
    margin-bottom: 10px;
}

.comments-list {
    list-style-type: none;
    padding-left: 0;
}

.comment-item {
    background-color: #ffecd9;
    /* border: 1px solid #ddd; */
    border-radius: 5px;
    margin-bottom: 10px;
    padding: 5px;
}

.post-actions {
    display: flex;
    gap: 10px;
}

.edit-button, .delete-button {
    padding: 8px 16px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.edit-button {
    background-color: #ff8c00;
    color: white;
}

.delete-button {
    background-color: #dc3545;
    color: white;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  overflow-y: auto;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  text-align: center;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.confirm-button, .cancel-button {
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
}

.confirm-button {
  background-color: #ffb347;
  color: white;
}

.cancel-button {
  background-color: #6c757d;
  color: white;
}

.add-post-button {
  margin-left: 20px;
  display: inline-block;
  padding: 10px 20px;
  background-color: #ff9a3b;
  color: white;
  font-size: 18px;
  text-decoration: none;
  border-radius: 5px;
}

.add-post-button:hover {
  background-color: #ffb347;
}

/* Styling for Add Post Modal */
.add-post-modal .modal-content {
  width: 400px;
  padding-bottom: 20px;
  margin-bottom: 35px;
  text-align: left;
  border-radius: 8px;
}

.add-post-modal label {
  display: block;
  font-size: 14px;
  margin-top: 5px;
  font-weight: bold;
}

.add-post-modal input[type="file"],
.add-post-modal input[type="text"],
.add-post-modal textarea {
  width: 100%;
  padding: 8px;
  margin-top: 2px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
}

.add-post-modal textarea {
  height: 65px;
  resize: vertical;
}

.add-post-modal .modal-actions {
  margin-top: 5px;
  display: flex;
  justify-content: space-between;
}

.add-post-modal .confirm-button, .add-post-modal .cancel-button {
  padding: 10px 20px;
  font-size: 16px;
}

.add-post-modal .confirm-button {
  background-color: #ff9a3b;
  color: white;
}

.add-post-modal .cancel-button {
  background-color: #6c757d;
  color: white;
}

.add-post-modal #map {
  margin-top: 5px;
  height: 150px;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
}

</style>
