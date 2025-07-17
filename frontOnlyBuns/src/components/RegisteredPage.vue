<template>
    <div>
        <SignOutButton />

        <!-- Navigation Links for Authenticated User -->
        <div class="home-navigation">
            <ul>
              
              <li><router-link to="/following-posts"><i class="fas fa-users"></i> Following Posts</router-link></li>
              <li><a @click="checkAuthAndNavigateToTrends"><i class="fas fa-user"></i> Trends</a></li>
              <li><a @click="navigateToNearbyPosts"><i class="fas fa-user"></i> Nearby Posts</a></li>
             
              <li><router-link to="/chat"><i class="fas fa-comments"></i> Chat</router-link></li>
              <li><a @click="checkAuthAndNavigateToProfile"><i class="fas fa-user"></i> Profile</a></li>
            </ul>

            <!-- <button @click="goHome" class="home-button">Home</button> -->
           <button @click="openAddPostModal" class="add-post-button">Add Post</button>
        </div>

        <!-- Main Content Area -->
        <main class="home-content">
            <div class="posts-container">
                <div v-for="post in posts" :key="post.id" class="post">
                  <!-- <h3>Location:</h3> -->
                  <p>{{ post.location }}</p>

                    <img :src="post.imageUrl" alt="Post Image" class="post-image" />
                    <div class="post-description">
                        <h3>Description:</h3>
                        <p>{{ post.description }}</p>
                    </div>
                    <div class="likes-container">
                      <h3>Likes:4</h3>
                      <p>{{ post.countLikes }}</p>
                    </div>

                    
                    <ul class="comments-list">
                      <h3>Comments:</h3>
                        <li v-for="comment in post.comments" :key="comment.id" class="comment-item">
                            <strong>{{ comment.username }}:</strong> {{ comment.content }}
                        </li>
                    </ul>
                   

                  <div class="add-comment">
                    <textarea v-model="newCommentContent[post.id]" placeholder="Add a comment..."></textarea>
                    <button @click="addComment(post.id)">→</button>
                  </div>
                  <div v-if="errorMessages[post.id]" class="error-message">
                    {{ errorMessages[post.id] }}
                  </div>

                </div>
            </div>
        </main>

       
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
          newCommentContent: {},
          errorMessages: {},
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
        async addComment(postId) {
          const token = sessionStorage.getItem("token");
          const content = this.newCommentContent[postId];

          if (!content) {
            this.errorMessages[postId] = "Comment cannot be empty.";
            return;
          }

          try {
            const response = await fetch(`http://localhost:8080/comments/create`, {
              method: "POST",
              headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/x-www-form-urlencoded",
              },
              body: new URLSearchParams({ postId, content }),
            });

            if (response.ok) {
              const newComment = await response.json();
              const post = this.posts.find((p) => p.id === postId);
              if (post) {
                post.comments.push({
                  id: newComment.id,
                  content: newComment.content,
                  username: newComment.username,
                });
              }
              this.newCommentContent[postId] = "";
              this.errorMessages[postId] = null;
              console.log("Comment added successfully.");
            } else if (response.status === 429) {
              const errorData = await response.json();
              this.errorMessages[postId] = errorData.error || "You have reached the comment limit.";
            } else {
              this.errorMessages[postId] = "Failed to add comment. Please try again.";
              console.error("Failed to add comment:", await response.text());
            }
          } catch (error) {
            this.errorMessages[postId] = "Error adding comment. Please try again later.";
            console.error("Error adding comment:", error.message);
          }
        },

      goToFollowingPosts() {
        window.location.href = "http://localhost:8081/";
    },
      onFileSelected(event) {
        this.selectedFile = event.target.files[0];
      },
      async createPost() {
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
              "Authorization": `Bearer ${token}`
            },
            body: formData,
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
            this.isRegistered = userType === 'REGISTERED';
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
                ///////////
                console.log("Postovi:", this.posts[0]);
                /////////
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
      checkAuthAndNavigateToProfile() {
    const token = sessionStorage.getItem('token');
    if (token) {
      this.$router.push('/profileN'); // Only redirect if user is logged in
    } else {
      this.$router.push('/login'); // Otherwise redirect to login
    }
   
  
},
navigateToNearbyPosts() {
      console.log("nadjiki")
      const token = sessionStorage.getItem('token');
    if (token) {
      this.$router.push({ path: '/nearby-posts', query: { token } });
    } else {
      console.error('Token not found. Redirecting to login.');
      this.$router.push('/login'); // Ako nema tokena, preusmeri na login stranicu
    }
    
   
  },
checkAuthAndNavigateToTrends(){
    
      this.$router.push('/trend');
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



.home-navigation ul li a:hover {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2); /* Dodaje senku */
  transform: translateY(-5px); /* Podiže link nagore za 5px */
}

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
  color: #333; /* Osnovna boja teksta */
  text-decoration: none; /* Uklanja podvlačenje */
  padding: 10px 15px; /* Razmak unutar linka */
  display: inline-block; /* Čini elementima da poštuju padding */
  transition: all 0.3s ease; /* Glatka tranzicija efekta */
  border-radius: 5px; /* Blago zaobljeni uglovi */
  cursor: pointer; /* Strelica za klikalne elemente */
}

.home-navigation ul li i {
    margin-right: 8px;
}






.posts-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 40px;
  justify-content: center;
  padding: 20px;
}

.post {
  background-color: #eaf4fc;
  border: 2px solid #4a90e2;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.12);
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.post:hover {
  transform: translateY(-5px);
}

.post-image {
  width: 80%;
  height: auto;
  object-fit: contain;
  display: block;
  margin: 0 auto;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.post-description {
  background-color: #ffffff;
  border-left: 4px solid #3a7bd5;
  padding: 12px;
  border-radius: 6px;
}

.post-description h3 {
  font-size: 22px;
  color: #1a4b7b;
  margin-bottom: 8px;
  font-weight: bold;
}

.post-description p {
  font-size: 16px;
  color: #444;
  line-height: 1.6;
}

.comments-list {
  margin-top: 15px;
  padding-left: 15px;
}

.comment-item {
  background-color: #f0f7ff;
  border-radius: 4px;
  padding: 6px 10px;
  margin-bottom: 6px;
  font-size: 14px;
}

.likes-container {
  display: flex;
  align-items: center;
  margin-top: 10px;
  font-weight: bold;
  color: #1a4b7b;
}

.add-comment {
  display: flex;
  gap: 10px;
  margin-top: 12px;
}

.add-comment textarea {
  flex: 1;
  border-radius: 6px;
  border: 1px solid #bcdffb;
  padding: 8px;
}

.add-comment button {
  background-color: #3a7bd5;
  color: white;
  padding: 8px 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.add-comment button:hover {
  background-color: #26547c;
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
modal-actions {
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
  background-color: #524db5;
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
  background-color: #695dd4;
  color: white;
  font-size: 18px;
  text-decoration: none;
  border-radius: 5px;
}

.add-post-button:hover {
  background-color: #0056b3;
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
  background-color: #5157c0;
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

.likes-container {
  display: flex; /* Postavlja elemente jedan pored drugog */
  align-items: center; /* Poravnava ih vertikalno na sredinu */
  gap: 10px; /* Razmak između h3 i p */
  margin-left: 20px; /* Pomera likove udesno */
}

.error-message {
  color: red;
  font-size: 14px;
  margin-top: 5px;
}

</style>