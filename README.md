# Használatfigyelő Android Alkalmazás

Egy megbízható Android alkalmazás, amely nyomon követi az alkalmazások használatát a telefonon - mely alkalmazások futnak, mennyi ideig és mikor.

## Főbb funkciók

- ✅ **Alkalmazáshasználat nyomon követése**: Automatikus rögzítés az alkalmazások használati idejéről
- ✅ **Napi naplók**: Pontos napi statisztikák az összes alkalmazásról
- ✅ **Dátum szűrés**: Szűrés ma, tegnap, ez a hét, ez a hónap, vagy összes adat szerint
- ✅ **Exportálás**: CSV és JSON formátumban exportálható adatok
- ✅ **Adatkezelés**: Régi adatok törlése vagy teljes adatbázis törlése
- ✅ **Root hozzáférés NEM szükséges**: Az Android beépített UsageStatsManager API-t használja

## Telepítés és beállítás

### Előfeltételek

- Android 8.0 (API 26) vagy újabb
- Android Studio Hedgehog vagy újabb (fejlesztéshez)

### Telepítési lépések

1. **Projekt klónozása vagy letöltése**
   ```bash
   cd /home/anna/lineages/használatfigyelő
   ```

2. **Android Studio-ban megnyitás**
   - Nyissa meg az Android Studio-t
   - Válassza a "Open an Existing Project" opciót
   - Navigáljon a projekt mappájához

3. **Gradle szinkronizálás**
   - Az Android Studio automatikusan letölti a szükséges függőségeket
   - Várja meg, amíg a Gradle build befejeződik

4. **APK buildelése**
   - Válassza a `Build > Build Bundle(s) / APK(s) > Build APK(s)` menüpontot
   - Várja meg a build befejezését
   - Az APK fájl az `app/build/outputs/apk/debug/` mappában lesz elérhető

5. **Telepítés a telefonra**
   - Kapcsolja be a "Ismeretlen forrásokból történő telepítés" opciót a telefon beállításaiban
   - Másolja az APK fájlt a telefonra
   - Nyissa meg az APK fájlt a telefonon és telepítse

### Engedélyek beállítása

**FONTOS**: Az alkalmazás működéséhez szükséges a "Használati hozzáférés" engedély megadása.

1. **Használati statisztikák engedély megadása**:
   - Nyissa meg az alkalmazást
   - Ha megjelenik a figyelmeztetés, kattintson a "Engedély megadása" gombra
   - Vagy manuálisan: **Beállítások > Alkalmazások > Speciális hozzáférés > Használati hozzáférés**
   - Keresse meg a "Használatfigyelő" alkalmazást a listában
   - Kapcsolja be a kapcsolót

2. **Tárhely engedély** (csak Android 10 és régebbi verziókhoz):
   - Az exportáláshoz szükséges lehet
   - Android 11+ esetén automatikusan működik

## Használati útmutató

### Főképernyő

- **Dátum szűrők**: Válassza ki a megjeleníteni kívánt időszakot (Ma, Tegnap, Ez a hét, Ez a hónap, Összes)
- **Frissítés gomb**: Manuálisan indítja az adatok gyűjtését
- **Exportálás gomb**: Megnyitja az exportálási képernyőt
- **Beállítások gomb** (jobb alsó sarok): Megnyitja a beállításokat

### Adatok megtekintése

Az alkalmazások listája a következő információkat jeleníti meg:
- **Alkalmazás neve**: Az alkalmazás megjelenített neve
- **Használati idő**: Mennyi ideig volt használatban (óra:perc formátumban)
- **Utolsó használat**: Mikor használták utoljára
- **Indítások száma**: Hányszor indították el

### Exportálás

1. Kattintson az "Exportálás" gombra a főképernyőn
2. Válassza ki a kívánt formátumot:
   - **CSV formátum**: Táblázatkezelő programokban (Excel, Google Sheets) megnyitható
   - **JSON formátum**: Programozási célokra
3. Vagy használja a gyors exportálási opciókat:
   - **Ma exportálása**: Csak a mai adatok
   - **Ez a hét exportálása**: Csak az aktuális hét adatai
4. Az exportált fájlok a **Letöltések** mappában lesznek elérhetők

### Beállítások

- **Engedélyek**: Ellenőrzi és beállítja a szükséges engedélyeket
- **Adatok gyűjtése most**: Azonnali adatgyűjtés indítása
- **30 napnál régebbi adatok törlése**: Régi adatok törlése a tárhely felszabadítása érdekében
- **Összes adat törlése**: Teljes adatbázis törlése (visszavonhatatlan!)

### Adatok törlése

- **Menüből**: A főképernyő jobb felső sarkában található menüben válassza az "Adatok törlése" opciót
- **Beállításokból**: A beállítások képernyőn található törlési opciók

## Technikai részletek

### Használt technológiák

- **Kotlin**: Modern Android fejlesztési nyelv
- **Room Database**: Helyi adatbázis az adatok tárolásához
- **UsageStatsManager API**: Android beépített API az alkalmazáshasználat követéséhez
- **Material Design 3**: Modern, letisztult felhasználói felület
- **MVVM architektúra**: Tiszta kód struktúra

### Engedélyek

- `PACKAGE_USAGE_STATS`: Használati statisztikák elérése (manuálisan kell engedélyezni)
- `QUERY_ALL_PACKAGES`: Alkalmazások listázása (Android 11+)
- `WRITE_EXTERNAL_STORAGE`: Exportáláshoz (csak Android 10 és régebbi)

### Adatbázis

Az alkalmazás Room adatbázist használ az adatok tárolásához. Az adatbázis a következő információkat tárolja:
- Alkalmazás neve és package neve
- Használati idő (milliszekundumban)
- Utolsó használat időbélyege
- Indítások száma
- Dátum (nap kezdete)

### Teljesítmény

- Az alkalmazás háttérben fut, minimális erőforrásigényt mutat
- Az adatok gyűjtése aszinkron módon történik, nem blokkolja a felhasználói felületet
- Az adatbázis optimalizálva van a gyors lekérdezésekhez

## Gyakori problémák és megoldások

### Az alkalmazás nem mutat adatokat

1. **Ellenőrizze az engedélyeket**: 
   - Menjen a Beállítások > Alkalmazások > Speciális hozzáférés > Használati hozzáférés menüpontra
   - Győződjön meg róla, hogy a "Használatfigyelő" alkalmazás be van kapcsolva

2. **Frissítse az adatokat**:
   - Kattintson a "Frissítés" gombra a főképernyőn
   - Várjon néhány másodpercet az adatok gyűjtésére

### Az exportálás nem működik

- **Android 10 és régebbi**: Ellenőrizze, hogy meg van-e adva a tárhely engedély
- **Android 11+**: Az exportált fájlok az alkalmazás saját mappájában lesznek (Letöltések mappa)

### Az alkalmazás nem fut háttérben

- Az alkalmazás nem fut folyamatosan háttérben
- Az adatok gyűjtése akkor történik, amikor:
  - Megnyitja az alkalmazást
  - Kattint a "Frissítés" gombra
  - A beállításokban kattint az "Adatok gyűjtése most" gombra

## Biztonság és adatvédelem

- **Helyi tárolás**: Minden adat csak a telefonon tárolódik, nincs külső szerver
- **Nincs internet hozzáférés**: Az alkalmazás nem küld adatokat külső szerverekre
- **Saját tulajdonú telefon**: Csak saját tulajdonú eszközön használja
- **Adatok törlése**: Bármikor törölheti az összes adatot a beállításokból

## Fejlesztési információk

### Build információk

- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### Projekt struktúra

```
app/
├── src/main/
│   ├── java/hu/anna/hasznalatfigyelo/
│   │   ├── data/          # Adatbázis entitások és DAO-k
│   │   ├── service/       # Háttér szolgáltatások
│   │   ├── ui/            # Felhasználói felület (Activity-k, ViewModel-ek)
│   │   └── util/          # Segédfunkciók
│   ├── res/               # Erőforrások (layout, string, stb.)
│   └── AndroidManifest.xml
```

## Támogatás

Ha problémába ütközik vagy kérdése van, ellenőrizze:
1. Az engedélyek beállítását
2. Az Android verzióját (minimum 8.0 szükséges)
3. Az alkalmazás verzióját

## Licenc

Ez az alkalmazás személyes használatra készült. A kódot saját felelősségre használhatja és módosíthatja.

## Frissítések

### Verzió 1.0.0
- Kezdeti kiadás
- Alapvető használatfigyelési funkciók
- CSV és JSON exportálás
- Dátum szűrési lehetőségek

