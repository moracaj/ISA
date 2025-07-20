<template>
  <div>
    <SignOutButton />

    <div class="nav-section">
      <ul>
        <li><router-link to="/following-posts"><i class="fas fa-users"></i> Following Posts</router-link></li>
        <li><router-link to="/trends"><i class="fas fa-chart-line"></i> Trends</router-link></li>
        <li><router-link to="/nearby-posts"><i class="fas fa-map-marker-alt"></i> Nearby Posts</router-link></li>
        <li><router-link to="/chat"><i class="fas fa-comments"></i> Chat</router-link></li>
        <li><router-link to="/profile"><i class="fas fa-user"></i> Profile</router-link></li>
      </ul>
      <button @click="openPostModal" class="btn-add-post">Add Post</button>
    </div>

    <main class="content-area">
      <div class="post-wrapper">
        <div v-for="entry in userPosts" :key="entry.id" class="post-card">
          <img :src="entry.imageUrl" alt="Post Image" class="post-thumbnail" />
          <div class="post-info">
            <h3>Description:</h3>
            <p class="desc-text">{{ entry.description }}</p>
            <div class="stats">
              <span>❤️ {{ entry.countLikes }} Likes</span>
              <span>💬 {{ entry.comments.length }} Comments</span>
            </div>
            <h3>Comments:</h3>
            <ul class="comment-list">
              <li v-for="comment in entry.comments" :key="comment.id" class="comment-block">
                <strong>{{ comment.username }}:</strong> {{ comment.content }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </main>

    <div v-if="displayAddPostModal" class="modal-wrapper">
      <div class="modal-inner">
        <h2>Create New Post</h2>
        <label for="img">Image:</label>
        <input type="file" @change="handleImage" id="img" />
        <label for="desc">Description:</label>
        <textarea v-model="postDraft.description" id="desc" placeholder="Type something..."></textarea>
        <label for="loc">Location:</label>
        <input v-model="postDraft.location" id="loc" placeholder="Or select on map" />
        <div id="map" style="height: 300px; margin-top: 20px;"></div>
        <div class="modal-actions">
          <button @click="submitNewPost" class="confirm">Save</button>
          <button @click="closePostModal" class="cancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SignOutButton from './SignOutButton.vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

export default {
  components: { SignOutButton },
  data() {
    return {
      userPosts: [],
      isUserVerified: false,
      displayAddPostModal: false,
      postDraft: {
        description: '',
        location: '',
        latitude: '',
        longitude: ''
      },
      map: null,
      marker: null,
      uploadedImage: null,
    };
  },
  mounted() {
    this.$nextTick(() => {
      if (this.displayAddPostModal) {
        this.initMap();
      }
    });
  },
  async created() {
    await this.verifyUser();
    const token = sessionStorage.getItem('token');
    if (this.isUserVerified && token) {
      this.loadUserPosts();
    }
  },
  methods: {
    handleImage(event) {
      this.uploadedImage = event.target.files[0];
    },
    async submitNewPost() {
      const token = sessionStorage.getItem('token');
      const formData = new FormData();
      formData.append('imageFile', this.uploadedImage);
      formData.append('description', this.postDraft.description);
      formData.append('latitude', this.postDraft.latitude);
      formData.append('longitude', this.postDraft.longitude);
      formData.append('location', this.postDraft.location);
      try {
        const response = await fetch('http://localhost:8080/posts/create', {
          method: 'POST',
          headers: { Authorization: `Bearer ${token}` },
          body: formData,
        });
        if (response.ok) {
          const created = await response.json();
          this.userPosts.unshift(created);
          this.closePostModal();
        }
      } catch (err) {
        console.error('Error creating post:', err);
      }
    },
    async verifyUser() {
      const userType = sessionStorage.getItem('userType');
      this.isUserVerified = userType === 'ROLE_REGISTERED';
    },
    async loadUserPosts() {
      const token = sessionStorage.getItem('token');
      try {
        const response = await fetch('http://localhost:8080/posts/my-posts', {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
        if (response.ok) {
          this.userPosts = await response.json();
        }
      } catch (err) {
        console.error('Error loading posts:', err);
      }
    },
    openPostModal() {
      this.displayAddPostModal = true;
      this.$nextTick(() => this.initMap());
    },
    closePostModal() {
      this.displayAddPostModal = false;
      this.resetForm();
    },
    initMap() {
      const mapDiv = document.getElementById('map');
      if (!mapDiv) return;

      this.map = L.map('map').setView([44.0165, 21.0059], 7);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 }).addTo(this.map);

      this.map.on('click', (e) => {
        this.postDraft.latitude = e.latlng.lat;
        this.postDraft.longitude = e.latlng.lng;
        if (this.marker) {
          this.marker.setLatLng(e.latlng);
        } else {
          this.marker = L.marker(e.latlng).addTo(this.map);
        }
        this.fetchAddress(e.latlng.lat, e.latlng.lng);
      });
    },
    async fetchAddress(lat, lng) {
      try {
        const res = await fetch(`https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json&accept-language=en`);
        const data = await res.json();
        if (data && data.address) {
          const city = data.address.city || data.address.town || data.address.village || 'Unknown city';
          const country = data.address.country || 'Unknown country';
          this.postDraft.location = `${city}, ${country}`;
        } else {
          this.postDraft.location = 'Unknown location';
        }
      } catch {
        this.postDraft.location = 'Error fetching location';
      }
    },
    resetForm() {
      this.postDraft = {
        description: '',
        location: '',
        latitude: '',
        longitude: ''
      };
      if (this.marker) {
        this.map.removeLayer(this.marker);
      }
    },
  },
};
</script>
<style scoped>
.nav-section {
  margin-bottom: 20px;
  text-align: center;
}
.nav-section ul {
  list-style: none;
  padding: 0;
}
.nav-section li {
  display: inline;
  margin-right: 15px;
}
.nav-section a {
  font-size: 18px;
  color: #d35400;
  text-decoration: none;
}
.nav-section i {
  margin-right: 6px;
}
.content-area {
  margin-top: 20px;
}
.post-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  justify-content: space-between;
}
.post-card {
  flex: 1 1 calc(25% - 20px);
  background: #f9f9f9;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
.post-thumbnail {
  max-width: 100%;
  border-radius: 5px;
}
.comment-list {
  padding-left: 0;
  list-style: none;
}
.comment-block {
  background: #ffecd9;
  border-radius: 5px;
  padding: 5px;
  margin-bottom: 8px;
}
.btn-add-post {
  margin-left: 20px;
  padding: 10px 20px;
  background: #ff9a3b;
  color: white;
  border-radius: 5px;
  font-size: 16px;
  border: none;
}
.btn-add-post:hover {
  background: #ffb347;
}
.modal-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-inner {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
}
.modal-inner label {
  display: block;
  margin-top: 8px;
  font-weight: bold;
}
.modal-inner input,
.modal-inner textarea {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.modal-inner textarea {
  resize: vertical;
}
.modal-actions {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
}
.confirm {
  background: #ff9a3b;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 5px;
}
.cancel {
  background: #6c757d;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 5px;
}
#map {
  margin-top: 8px;
  height: 150px;
  width: 100%;
  border-radius: 5px;
  border: 1px solid #ccc;
}
</style>
