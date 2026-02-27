# 📸 Instagram Clone - Backend (Spring Boot)

Bu proje, Dokuz Eylül Üniversitesi Bilgisayar Mühendisliği kapsamında geliştirilen, Spring Boot tabanlı bir sosyal medya backend uygulamasıdır.

## 📂 Proje Yapısı

```text
instagram/
└── backend/
    ├── src/
    │   └── main/java/com/deuceng/instagram/
    │       ├── Controller/    # API Endpoint'leri
    │       ├── DTO/           # Veri Transfer Objeleri
    │       ├── Entity/        # Veritabanı Modelleri
    │       ├── Repository/    # Veritabanı Erişim Katmanı
    │       ├── Security/      # JWT ve Güvenlik Yapılandırması
    │       └── Service/       # İş Mantığı Katmanı
    ├── pom.xml                # Maven Bağımlılıkları
    └── README.md              # Proje Dökümantasyonu

## 🚀 Başlangıç

Projeyi yerelinizde çalıştırmak için aşağıdaki adımları takip edin:

1. **Konfigürasyon:** `src/main/resources/application.properties.example` dosyasının bir kopyasını oluşturun ve adını `application.properties` olarak değiştirin.

### 2. Veritabanı Ayarları (PostgreSQL)
- Yerelinizde `instagram_db` adında bir veritabanı oluşturun.
- `src/main/resources/application.properties.example` dosyasının adını `application.properties` olarak değiştirin.
- Dosya içindeki `spring.datasource.username` ve `password` alanlarını kendi PostgreSQL bilgilerinizle doldurun.

### 3. Görsel Yükleme Ayarları (Cloudinary)
Bu proje fotoğrafları bulutta saklamak için Cloudinary kullanır. Sistemin çalışması için IntelliJ IDEA üzerinden şu **Ortam Değişkenlerini (Environment Variables)** tanımlamanız gerekir:

- `CLOUDINARY_NAME`: Sizin cloud isminiz
- `CLOUDINARY_KEY`: API Anahtarınız
- `CLOUDINARY_SECRET`: API Secret numaranız

> **Not:** Bu değişkenleri `Run/Debug Configurations -> Edit Configurations -> Environment Variables` kısmından ekleyebilirsiniz.

### 4. Uygulamayı Çalıştırma
- Projeyi IntelliJ ile açın ve Maven bağımlılıklarının yüklenmesini bekleyin.
- `InstagramApplication.java` dosyasını çalıştırın.
- Uygulama `http://localhost:8080` portunda ayağa kalkacaktır.