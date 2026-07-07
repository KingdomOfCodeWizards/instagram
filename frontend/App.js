import React, { useState, useEffect } from 'react';
import { 
  StyleSheet, 
  Text, 
  View, 
  TextInput, 
  TouchableOpacity, 
  SafeAreaView, 
  KeyboardAvoidingView, 
  Platform,
  ActivityIndicator,
  ScrollView,
  Image,
  FlatList,
  Modal
} from 'react-native';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; 

export default function App() {
  const [currentScreen, setCurrentScreen] = useState('login');
  const [loading, setLoading] = useState(false);
  const [token, setToken] = useState(''); 
  
  // Login & Register State'leri
  const [loginUsername, setLoginUsername] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  // Ana Sayfa (Feed) State'leri
  const [posts, setPosts] = useState([]);
  const [refreshing, setRefreshing] = useState(false);

  // Yeni Post Ekleme State'leri (Modal ve Form alanları)
  const [isPostModalVisible, setIsPostModalVisible] = useState(false);
  const [postDescription, setPostDescription] = useState('');
  const [postMediaUrl, setPostMediaUrl] = useState('');
  const [postLoading, setPostLoading] = useState(false);

  // ================= BACKEND GİRİŞ BAĞLANTISI =================
  const handleLogin = async () => {
    if (!loginUsername || !loginPassword) {
      alert('Lütfen kullanıcı adı ve şifrenizi girin.');
      return;
    }
    setLoading(true);
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, {
        identifier: loginUsername,
        password: loginPassword
      });

      if (response.data) {
        const receivedToken = response.data.token || response.data; 
        setToken(receivedToken);
        alert('Giriş Başarılı!');
        setCurrentScreen('feed');
      }
    } catch (error) {
      alert(error.response?.data?.message || 'Giriş yapılamadı.');
    } finally {
      setLoading(false);
    }
  };

  // ================= BACKEND KAYIT BAĞLANTISI =================
  const handleRegister = async () => {
    if (password !== confirmPassword) { alert('Şifreler uyuşmuyor!'); return; }
    if (!name || !surname || !username || !email || !password) { alert('Lütfen alanları doldurun.'); return; }
    setLoading(true);
    try {
      await axios.post(`${API_BASE_URL}/auth/register`, {
        name, surname, username, password, email, confirmPassword, isPrivate: false
      });
      alert('Kayıt başarılı! Giriş yapabilirsiniz.');
      setCurrentScreen('login');
    } catch (error) {
      alert(error.response?.data?.message || 'Kayıt hatası.');
    } finally {
      setLoading(false);
    }
  };

  // ================= BACKEND GÖNDERİLERİ (POSTS) ÇEKME =================
  const fetchPosts = async () => {
    setRefreshing(true);
    try {
      // Senin PostController içindeki listeleme ucuyla konuşur
      const response = await axios.get(`${API_BASE_URL}/api/posts`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      
      // Eğer backend veri döndüyse state'e yaz
      if(response.data && response.data.length > 0) {
        setPosts(response.data);
      } else {
        // Boşsa dummy verileri göster ki ekran boş kalmasın
        loadDummyPosts();
      }
    } catch (error) {
      console.error("Postlar çekilemedi, test verileri yükleniyor:", error);
      loadDummyPosts();
    } finally {
      setRefreshing(false);
    }
  };

  const loadDummyPosts = () => {
    setPosts([
      { id: 'd1', user: { username: 'deneme_user' }, description: 'İlk Instagram Paylaşımı! 🚀', mediaUrl: 'https://picsum.photos/400/400' },
      { id: 'd2', user: { username: 'software_engineer' }, description: 'Spring Boot + React Native harika ikili.', mediaUrl: 'https://picsum.photos/401/401' }
    ]);
  };

  useEffect(() => {
    if (currentScreen === 'feed' && token) {
      fetchPosts();
    }
  }, [currentScreen, token]);

 
 
 // ================= BACKEND YENİ POST EKLEME BAĞLANTISI =================
  const handleCreatePost = async () => {
    if (!postDescription || !postMediaUrl) {
      alert('Lütfen açıklama ve bir görsel linki girin.');
      return;
    }

    setPostLoading(true);
    try {
      const formData = new FormData();
      
      // 1. Açıklama metnini (caption) ekliyoruz
      formData.append('caption', postDescription); 

      // 2. Tarayıcıda linki gerçek bir ham dosya verisine (Blob) dönüştürüyoruz
      const responseBlob = await fetch(postMediaUrl);
      const blob = await responseBlob.blob();

      // 3. Dönüştürülen bu gerçek dosyayı backend'in beklediği 'file' ismiyle pakete koyuyoruz
      formData.append('file', blob, 'instagram_photo.jpg');

      // Backend API'sine istek atıyoruz
      await axios.post(`${API_BASE_URL}/api/posts/upload`, formData, {
        headers: { 
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        } 
      });

      alert('Gönderi başarıyla paylaşıldı!');
      
      // Formu temizle ve kapat
      setPostDescription('');
      setPostMediaUrl('');
      setIsPostModalVisible(false);
      
      // Akışı canlı olarak güncelle
      fetchPosts();
    } catch (error) {
      console.error("Post paylaşma hatası:", error);
      alert(error.response?.data?.message || 'Gönderi paylaşılırken bir hata oluştu.');
    } finally {
      setPostLoading(false);
    }
  };

  const dummyStories = [
    { id: 1, name: 'Senin Hikayen' }, { id: 2, name: 'ahmet_1' }, { id: 3, name: 'ece.dev' }, { id: 4, name: 'hoca_anlatiyor' }
  ];

  return (
    <SafeAreaView style={styles.container}>
      {currentScreen === 'login' && (
        /* GİRİŞ EKRANI */
        <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'} style={styles.innerContainer}>
          <Text style={styles.logoText}>Instagram</Text>
          <View style={styles.formContainer}>
            <TextInput style={styles.input} placeholder="Kullanıcı adı veya e-posta" placeholderTextColor="#999" value={loginUsername} onChangeText={setLoginUsername} autoCapitalize="none" />
            <TextInput style={styles.input} placeholder="Şifre" placeholderTextColor="#999" secureTextEntry value={loginPassword} onChangeText={setLoginPassword} autoCapitalize="none" />
            <TouchableOpacity style={styles.button} onPress={handleLogin} disabled={loading}>
              {loading ? <ActivityIndicator color="#fff" /> : <Text style={styles.buttonText}>Giriş Yap</Text>}
            </TouchableOpacity>
            <View style={styles.footer}><Text style={styles.footerText}>Hesabın yok mu? </Text>
              <TouchableOpacity onPress={() => setCurrentScreen('register')}><Text style={styles.footerLink}>Kaydol.</Text></TouchableOpacity>
            </View>
          </View>
        </KeyboardAvoidingView>
      )}

      {currentScreen === 'register' && (
        /* KAYIT EKRANI */
        <ScrollView contentContainerStyle={styles.scrollContainer} showsVerticalScrollIndicator={false}>
          <Text style={styles.logoText}>Instagram</Text>
          <View style={styles.formContainer}>
            <TextInput style={styles.input} placeholder="Ad" placeholderTextColor="#999" value={name} onChangeText={setName} />
            <TextInput style={styles.input} placeholder="Soyad" placeholderTextColor="#999" value={surname} onChangeText={setSurname} />
            <TextInput style={styles.input} placeholder="E-posta" placeholderTextColor="#999" value={email} onChangeText={setEmail} keyboardType="email-address" autoCapitalize="none" />
            <TextInput style={styles.input} placeholder="Kullanıcı Adı" placeholderTextColor="#999" value={username} onChangeText={setUsername} autoCapitalize="none" />
            <TextInput style={styles.input} placeholder="Şifre" placeholderTextColor="#999" secureTextEntry value={password} onChangeText={setPassword} autoCapitalize="none" />
            <TextInput style={styles.input} placeholder="Şifreyi Tekrar Girin" placeholderTextColor="#999" secureTextEntry value={confirmPassword} onChangeText={setConfirmPassword} autoCapitalize="none" />
            <TouchableOpacity style={styles.button} onPress={handleRegister} disabled={loading}>
              {loading ? <ActivityIndicator color="#fff" /> : <Text style={styles.buttonText}>Kaydol</Text>}
            </TouchableOpacity>
            <View style={styles.footer}><Text style={styles.footerText}>Hesabın var mı? </Text>
              <TouchableOpacity onPress={() => setCurrentScreen('login')}><Text style={styles.footerLink}>Giriş Yap.</Text></TouchableOpacity>
            </View>
          </View>
        </ScrollView>
      )}

      {currentScreen === 'feed' && (
        /* INSTAGRAM ANA SAYFA (FEED) ARAYÜZÜ */
        <View style={{ flex: 1 }}>
          {/* Üst Logo ve Aksiyon Barı */}
          <View style={styles.navBar}>
            <Text style={[styles.logoText, { fontSize: 26, marginBottom: 0 }]}>Instagram</Text>
            <View style={{ flexDirection: 'row', gap: 15, alignItems: 'center' }}>
              {/* Yeni Post Ekleme Tetikleyici Buton ➕ */}
              <TouchableOpacity onPress={() => setIsPostModalVisible(true)}>
                <Text style={{fontSize: 22, fontWeight: 'bold', color: '#000'}}>＋</Text>
              </TouchableOpacity>
              <TouchableOpacity><Text style={{fontSize: 20}}>❤️</Text></TouchableOpacity>
              <TouchableOpacity><Text style={{fontSize: 20}}>💬</Text></TouchableOpacity>
            </View>
          </View>

          {/* Gönderiler Listesi */}
          <FlatList
            data={posts}
            keyExtractor={(item) => item.id.toString()}
            refreshing={refreshing}
            onRefresh={fetchPosts}
            ListHeaderComponent={() => (
              /* Üst Kısım: Hikayeler Akışı */
              <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.storiesContainer}>
                {dummyStories.map((story) => (
                  <View key={story.id} style={styles.storyWrapper}>
                    <View style={styles.storyCircle}>
                      <View style={styles.storyInnerCircle} />
                    </View>
                    <Text style={styles.storyName} numberOfLines={1}>{story.name}</Text>
                  </View>
                ))}
              </ScrollView>
            )}
            renderItem={({ item }) => (
              /* Her Bir Instagram Post Kartı */
              <View style={styles.postContainer}>
                <View style={styles.postHeader}>
                  <View style={styles.avatarCircle} />
                  <Text style={styles.postUsername}>{item.user?.username || 'kullanıcı'}</Text>
                </View>

                <Image source={{ uri: item.mediaUrl || 'https://picsum.photos/400/400' }} style={styles.postImage} />

                <View style={styles.postActions}>
                  <TouchableOpacity style={styles.actionButton}><Text style={{fontSize: 18}}>🤍</Text></TouchableOpacity>
                  <TouchableOpacity style={styles.actionButton}><Text style={{fontSize: 18}}>💬</Text></TouchableOpacity>
                  <TouchableOpacity style={styles.actionButton}><Text style={{fontSize: 18}}>✈️</Text></TouchableOpacity>
                </View>

                <View style={styles.postDetails}>
                  <Text style={styles.descriptionText}>
                    <Text style={styles.boldUsername}>{item.user?.username || 'kullanıcı'} </Text>
                    {item.description}
                  </Text>
                </View>
              </View>
            )}
          />

          {/* ================= YENİ POST EKLEME MODAL PANELİ ================= */}
          <Modal visible={isPostModalVisible} animationType="slide" transparent={false}>
            <SafeAreaView style={styles.modalContainer}>
              <View style={styles.modalHeader}>
                <TouchableOpacity onPress={() => setIsPostModalVisible(false)}>
                  <Text style={styles.modalCloseText}>İptal</Text>
                </TouchableOpacity>
                <Text style={styles.modalTitle}>Yeni Gönderi</Text>
                <TouchableOpacity onPress={handleCreatePost} disabled={postLoading}>
                  {postLoading ? <ActivityIndicator color="#0095F6" /> : <Text style={styles.modalShareText}>Paylaş</Text>}
                </TouchableOpacity>
              </View>

              <View style={{ padding: 20 }}>
                {/* Geçici Görsel Önizleme Alanı */}
                {postMediaUrl ? (
                  <Image source={{ uri: postMediaUrl }} style={styles.previewImage} onError={() => alert('Geçersiz resim linki!')} />
                ) : (
                  <View style={styles.previewPlaceholder}>
                    <Text style={{ color: '#8E8E8E' }}>Resim URL'si girildiğinde önizleme burada görünecek</Text>
                  </View>
                )}

                <TextInput 
                  style={[styles.input, { height: 50, marginTop: 20 }]} 
                  placeholder="Fotoğraf İnternet Linki (URL)..." 
                  placeholderTextColor="#999"
                  value={postMediaUrl}
                  onChangeText={setPostMediaUrl}
                />

                <TextInput 
                  style={[styles.input, { height: 80, paddingTop: 10, textAlignVertical: 'top' }]} 
                  placeholder="Bir açıklama yaz..." 
                  placeholderTextColor="#999"
                  multiline
                  value={postDescription}
                  onChangeText={setPostDescription}
                />
              </View>
            </SafeAreaView>
          </Modal>

        </View>
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#ffffff' },
  innerContainer: { flex: 1, justifyContent: 'center', alignItems: 'center', paddingHorizontal: 40 },
  scrollContainer: { paddingVertical: 40, paddingHorizontal: 40, alignItems: 'center' },
  logoText: { fontSize: 42, fontFamily: Platform.OS === 'ios' ? 'Chalkboard SE' : 'serif', fontWeight: 'bold', marginBottom: 15, color: '#000000' },
  formContainer: { width: '100%' },
  input: { width: '100%', height: 44, backgroundColor: '#FAFAFA', borderWidth: 1, borderColor: '#EFEDED', borderRadius: 5, paddingHorizontal: 15, marginBottom: 12, fontSize: 14, color: '#000' },
  button: { width: '100%', height: 44, backgroundColor: '#0095F6', borderRadius: 5, justifyContent: 'center', alignItems: 'center', marginTop: 8 },
  buttonText: { color: '#fff', fontWeight: 'bold', fontSize: 14 },
  footer: { flexDirection: 'row', justifyContent: 'center', marginTop: 25 },
  footerText: { color: '#8E8E8E', fontSize: 13 },
  footerLink: { color: '#0095F6', fontWeight: 'bold', fontSize: 13 },
  
  // Feed Styles
  navBar: { height: 50, borderBottomWidth: 1, borderBottomColor: '#DBDBDB', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 15 },
  storiesContainer: { paddingVertical: 10, paddingLeft: 10, borderBottomWidth: 1, borderBottomColor: '#F2F2F2', flexDirection: 'row' },
  storyWrapper: { alignItems: 'center', marginRight: 15, width: 65 },
  storyCircle: { width: 56, height: 56, borderRadius: 28, borderWidth: 2, borderColor: '#DE0046', justifyContent: 'center', alignItems: 'center' },
  storyInnerCircle: { width: 48, height: 48, borderRadius: 24, backgroundColor: '#DBDBDB' },
  storyName: { fontSize: 11, marginTop: 4, color: '#262626', width: '100%', textAlign: 'center' },
  
  // Post Styles
  postContainer: { marginBottom: 15 },
  postHeader: { flexDirection: 'row', alignItems: 'center', padding: 10 },
  avatarCircle: { width: 32, height: 32, borderRadius: 16, backgroundColor: '#DBDBDB', marginRight: 10 },
  postUsername: { fontWeight: 'bold', fontSize: 13, color: '#262626' },
  postImage: { width: '100%', height: 400, resizeMode: 'cover' },
  postActions: { flexDirection: 'row', padding: 10, gap: 10 },
  actionButton: { padding: 4 },
  postDetails: { paddingHorizontal: 14 },
  descriptionText: { fontSize: 14, color: '#262626', lineHeight: 18 },
  boldUsername: { fontWeight: 'bold' },

  // Modal Styles
  modalContainer: { flex: 1, backgroundColor: '#fff' },
  modalHeader: { height: 50, borderBottomWidth: 1, borderBottomColor: '#EFEDED', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 15 },
  modalCloseText: { fontSize: 16, color: '#000' },
  modalTitle: { fontSize: 16, fontWeight: 'bold', color: '#000' },
  modalShareText: { fontSize: 16, fontWeight: 'bold', color: '#0095F6' },
  previewImage: { width: '100%', height: 250, borderRadius: 8, resizeMode: 'cover' },
  previewPlaceholder: { width: '100%', height: 250, backgroundColor: '#FAFAFA', borderRadius: 8, borderRules: 1, borderColor: '#EFEDED', borderStyle: 'dashed', justifyContent: 'center', alignItems: 'center', padding: 20, textAlign: 'center' }
});