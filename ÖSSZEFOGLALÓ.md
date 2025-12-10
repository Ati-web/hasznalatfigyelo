# Használatfigyelő Android Alkalmazás - Összefoglaló

## Projekt Áttekintés

Ez az alkalmazás egy **megbízható, legális Android alkalmazáshasználat-figyelő**, amely nyomon követi, mely alkalmazások futnak a telefonon, mennyi ideig és mikor.

## Főbb Jellemzők

✅ **Root hozzáférés NEM szükséges** - Az Android beépített UsageStatsManager API-t használja
✅ **Play Áruház szabályzatnak megfelelő** - Csak standard Android API-kat használ
✅ **Minimális teljesítményigény** - Nem zavarja a telefon normál működését
✅ **Helyi adattárolás** - Minden adat csak a telefonon tárolódik, nincs külső szerver
✅ **Exportálási lehetőség** - CSV és JSON formátumban

## Technikai Specifikációk

### Minimum Követelmények
- **Android verzió**: 8.0 (API 26) vagy újabb
- **Engedélyek**: 
  - `PACKAGE_USAGE_STATS` (manuálisan kell engedélyezni)
  - `QUERY_ALL_PACKAGES` (Android 11+)
  - `WRITE_EXTERNAL_STORAGE` (csak Android 10 és régebbi, exportáláshoz)

### Használt Technológiák
- **Kotlin** - Modern Android fejlesztési nyelv
- **Room Database** - Helyi SQLite adatbázis
- **UsageStatsManager API** - Android beépített API
- **Material Design 3** - Modern UI
- **MVVM architektúra** - Tiszta kód struktúra

## Funkciók

### 1. Alkalmazáshasználat Nyomon Követése
- Automatikus rögzítés az alkalmazások használati idejéről
- Utolsó használat időbélyege
- Indítások száma (ahol elérhető)

### 2. Dátum Szűrés
- **Ma**: Csak a mai nap adatai
- **Tegnap**: Csak a tegnapi nap adatai
- **Ez a hét**: Az aktuális hét adatai
- **Ez a hónap**: Az aktuális hónap adatai
- **Összes**: Minden rögzített adat

### 3. Exportálás
- **CSV formátum**: Táblázatkezelő programokban (Excel, Google Sheets) megnyitható
- **JSON formátum**: Programozási célokra
- Gyors exportálási opciók: Ma, Ez a hét

### 4. Adatkezelés
- Régi adatok törlése (30 napnál régebbi)
- Teljes adatbázis törlése
- Manuális adatgyűjtés indítása

## Telepítés és Beállítás

### Gyors Telepítés

1. **APK Buildelése** (Android Studio)
   ```
   Build > Build Bundle(s) / APK(s) > Build APK(s)
   ```

2. **Telepítés a telefonra**
   - USB-n keresztül: `adb install app-debug.apk`
   - Vagy másolja az APK fájlt a telefonra és telepítse

3. **Engedélyek beállítása** (KRITIKUS!)
   - Nyissa meg az alkalmazást
   - Kattintson az "Engedély megadása" gombra
   - Vagy: Beállítások > Alkalmazások > Speciális hozzáférés > Használati hozzáférés
   - Kapcsolja BE a "Használatfigyelő" alkalmazást

4. **Első használat**
   - Kattintson a "Frissítés" gombra
   - Várjon néhány másodpercet az adatok gyűjtésére

### Becsült Beállítási Idő
- **APK buildelése**: 2-5 perc
- **Telepítés**: 1-2 perc
- **Engedélyek**: 1-2 perc
- **Első adatgyűjtés**: 10-30 másodperc
- **Összesen**: ~5-10 perc

## Adatok Tárolása

### Adatbázis Struktúra
- **Táblázat**: `app_usage`
- **Mezők**:
  - `id`: Egyedi azonosító
  - `packageName`: Alkalmazás package neve
  - `appName`: Alkalmazás megjelenített neve
  - `usageTimeMillis`: Használati idő milliszekundumban
  - `lastUsedTimestamp`: Utolsó használat időbélyege
  - `launchCount`: Indítások száma
  - `date`: Dátum (nap kezdete)
  - `createdAt`: Létrehozás időbélyege

### Adatok Megmaradása
✅ Újraindítás után
✅ Android frissítés után
✅ Alkalmazás frissítés után (ha ugyanaz az adatbázis verzió)
❌ Alkalmazás törlése után (minden adat elvész!)

## Biztonság és Adatvédelem

- ✅ **Helyi tárolás**: Minden adat csak a telefonon
- ✅ **Nincs internet hozzáférés**: Nem küld adatokat külső szerverekre
- ✅ **Saját tulajdonú telefon**: Csak saját eszközön használható
- ✅ **Adatok törlése**: Bármikor törölhető

## Projekt Struktúra

```
használatfigyelő/
├── app/
│   ├── src/main/
│   │   ├── java/hu/anna/hasznalatfigyelo/
│   │   │   ├── data/              # Adatbázis (Entity, DAO, Database)
│   │   │   ├── service/           # UsageStatsService
│   │   │   ├── ui/                # Activity-k, ViewModel-ek, Adapter-ek
│   │   │   └── util/              # Segédfunkciók (PermissionHelper, DateHelper)
│   │   ├── res/                   # Erőforrások
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── README.md                      # Részletes dokumentáció
├── TELEPÍTÉSI_ÚTMUTATÓ.md        # Telepítési lépések
└── ÖSSZEFOGLALÓ.md               # Ez a fájl
```

## Gyakori Problémák

### "Nincs rögzített adat" üzenet
**Megoldás**:
1. Ellenőrizze a használati hozzáférés engedélyt
2. Kattintson a "Frissítés" gombra
3. Használjon néhány alkalmazást, majd frissítse újra

### Az alkalmazás nem található a "Használati hozzáférés" listában
**Megoldás**:
1. Telepítse újra az alkalmazást
2. Nyissa meg legalább egyszer az alkalmazást
3. Próbálja újra a beállításokban

### Exportálás nem működik
**Megoldás**:
- Android 10 és régebbi: Ellenőrizze a tárhely engedélyt
- Android 11+: Az exportált fájlok a Letöltések mappában lesznek

## További Információk

- **Részletes dokumentáció**: `README.md`
- **Telepítési útmutató**: `TELEPÍTÉSI_ÚTMUTATÓ.md`
- **Kód dokumentáció**: Inline kommentek a forráskódban

## Verzió Információk

- **Verzió**: 1.0.0
- **Build verzió**: 1
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## Licenc és Használat

Ez az alkalmazás személyes használatra készült. A kódot saját felelősségre használhatja és módosíthatja.

---

**Fontos**: Ez az alkalmazás csak saját tulajdonú eszközön használható. Mások eszközeinek figyelése jogi következményekkel járhat!

