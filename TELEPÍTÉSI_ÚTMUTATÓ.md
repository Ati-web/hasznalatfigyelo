# Telepítési és Beállítási Útmutató

## Gyors Telepítés (Fizikai Hozzáférés)

### 1. lépés: APK Buildelése

1. Nyissa meg az Android Studio-t
2. Nyissa meg a projektet: `File > Open` > válassza ki a projekt mappáját
3. Várja meg, amíg a Gradle szinkronizálás befejeződik
4. Buildelés: `Build > Build Bundle(s) / APK(s) > Build APK(s)`
5. Az APK fájl helye: `app/build/outputs/apk/debug/app-debug.apk`

### 2. lépés: Telepítés a Telefonra

**Opció A: USB-n keresztül (ADB)**
```bash
# Kapcsolja be a USB hibakeresést a telefonon
# Beállítások > Telefon információ > Build szám (7x kattintás)
# Beállítások > Fejlesztői beállítások > USB hibakeresés (bekapcsolás)

# Telepítés ADB-vel
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Opció B: Fájlkezelőn keresztül**
1. Másolja az APK fájlt a telefonra (USB, email, cloud storage)
2. A telefonon: Fájlkezelő > APK fájl megnyitása
3. Engedélyezze az "Ismeretlen forrásokból történő telepítés" opciót, ha kéri
4. Telepítés gomb

### 3. lépés: Engedélyek Beállítása (KRITIKUS!)

**Használati statisztikák engedély:**

1. Nyissa meg az alkalmazást
2. Ha megjelenik a piros figyelmeztető kártya, kattintson a **"Engedély megadása"** gombra
3. Vagy manuálisan:
   - **Beállítások** (Settings)
   - **Alkalmazások** (Apps) vagy **Alkalmazások és értesítések** (Apps & notifications)
   - **Speciális hozzáférés** (Special access) vagy **Speciális alkalmazás hozzáférés** (Special app access)
   - **Használati hozzáférés** (Usage access)
   - Keresse meg a **"Használatfigyelő"** alkalmazást
   - Kapcsolja **BE** a kapcsolót

**Fontos**: Ez az engedély NÉLKÜL az alkalmazás NEM fog működni!

### 4. lépés: Első Használat

1. Nyissa meg az alkalmazást
2. Kattintson a **"Frissítés"** gombra az adatok gyűjtéséhez
3. Várjon néhány másodpercet
4. Az alkalmazások listája megjelenik a használati időkkel

## Távoli Telepítés (Nincs Fizikai Hozzáférés)

Ha nincs fizikai hozzáférés a telefonhoz, a következő lépéseket kell követnie:

### 1. APK Fájl Elkészítése

Ugyanaz, mint a "Gyors Telepítés" 1. lépése.

### 2. APK Fájl Küldése

- **Email**: Küldje el az APK fájlt önmagának, majd nyissa meg a telefonon
- **Cloud Storage**: Töltse fel Google Drive-ra, Dropbox-ra, stb., majd töltse le a telefonon
- **Messenger**: Küldje el valamilyen üzenetküldő alkalmazáson keresztül

### 3. Telepítési Útmutatás Küldése

Küldje el a következő útmutatást a telefon felhasználójának:

```
1. Nyissa meg az APK fájlt a telefonon
2. Ha kéri, engedélyezze az "Ismeretlen forrásokból történő telepítés" opciót
3. Telepítse az alkalmazást
4. Nyissa meg az alkalmazást
5. Kattintson az "Engedély megadása" gombra
6. A beállításokban keresse meg a "Használatfigyelő" alkalmazást
7. Kapcsolja BE a kapcsolót
8. Vissza az alkalmazásba, kattintson a "Frissítés" gombra
```

## Ellenőrzés: Működik-e az Alkalmazás?

### Sikeres Beállítás Jelei:

✅ Az alkalmazás megnyílik hiba nélkül
✅ Nincs piros figyelmeztető kártya az engedélyekről
✅ A "Frissítés" gombra kattintva adatok jelennek meg
✅ Az alkalmazások listája megjelenik használati időkkel

### Problémák Megoldása:

**Probléma**: "Nincs rögzített adat" üzenet
- **Megoldás**: 
  1. Ellenőrizze, hogy meg van-e adva a használati hozzáférés engedély
  2. Kattintson a "Frissítés" gombra
  3. Várjon néhány másodpercet
  4. Használjon néhány alkalmazást, majd frissítse újra

**Probléma**: Az alkalmazás nem található a "Használati hozzáférés" listában
- **Megoldás**: 
  1. Telepítse újra az alkalmazást
  2. Nyissa meg legalább egyszer az alkalmazást
  3. Próbálja újra a beállításokban

**Probléma**: Az exportálás nem működik
- **Megoldás**:
  - Android 10 és régebbi: Ellenőrizze a tárhely engedélyt
  - Android 11+: Az exportált fájlok a Letöltések mappában lesznek

## Automatikus Adatgyűjtés Beállítása

Az alkalmazás jelenleg **nem** fut folyamatosan háttérben. Az adatok gyűjtése akkor történik, amikor:

1. Megnyitja az alkalmazást (automatikusan)
2. Kattint a "Frissítés" gombra
3. A beállításokban kattint az "Adatok gyűjtése most" gombra

**Javaslat**: Nyissa meg az alkalmazást naponta egyszer, hogy naprakész adatokkal rendelkezzen.

## Adatok Újraindítás/FRissítés Után

✅ **Az adatok megmaradnak** újraindítás után
✅ **Az adatok megmaradnak** Android frissítés után
✅ **Az adatok megmaradnak** alkalmazás frissítés után (ha ugyanaz az adatbázis verzió)

⚠️ **Figyelem**: Ha törli az alkalmazást, az összes adat elvész!

## Becsült Beállítási Idő

- **APK buildelése**: 2-5 perc (első alkalommal hosszabb lehet)
- **Telepítés**: 1-2 perc
- **Engedélyek beállítása**: 1-2 perc
- **Első adatgyűjtés**: 10-30 másodperc

**Összesen**: ~5-10 perc

## Kérdések a Kezdéshez

Mielőtt elkezdené, győződjön meg arról, hogy tudja a választ ezekre:

1. **Milyen Android verzió fut a telefonon?**
   - Minimum: Android 8.0 (API 26)
   - Ellenőrzés: Beállítások > Telefon információ > Android verzió

2. **Van-e root hozzáférés?**
   - **NEM szükséges!** Az alkalmazás root nélkül működik.

3. **Van-e fizikai hozzáférés a telefonhoz?**
   - Ha igen: USB-n keresztül telepítheti (gyorsabb)
   - Ha nem: Email/cloud storage-n keresztül (lassabb, de működik)

4. **Milyen gyakran szeretné frissíteni az adatokat?**
   - Naponta egyszer: Nyissa meg az alkalmazást naponta
   - Többször naponta: Használja a "Frissítés" gombot

## További Segítség

Ha további segítségre van szüksége, olvassa el a `README.md` fájlt, amely részletesebb információkat tartalmaz a használatról és a funkciókról.

