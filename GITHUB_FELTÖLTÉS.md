# GitHub-ra feltöltés útmutató

## 1. Git beállítások (ha még nem állítottad be)

Ha még nem állítottad be a git felhasználói adataidat, futtasd le ezeket a parancsokat:

```bash
git config --global user.name "A GitHub felhasználóneved"
git config --global user.email "A GitHub email címed"
```

## 2. GitHub repository létrehozása

1. Lépj be a GitHub-ra: https://github.com
2. Kattints a jobb felső sarokban a **"+"** gombra, majd válaszd a **"New repository"** opciót
3. Töltsd ki az űrlapot:
   - **Repository name**: `hasznalatfigyelo` (vagy bármilyen nevet szeretnél)
   - **Description**: "Android alkalmazáshasználat-figyelő alkalmazás"
   - Válaszd ki, hogy **Public** vagy **Private** legyen
   - **NE** jelöld be a "Initialize this repository with a README" opciót (már van README)
   - Kattints a **"Create repository"** gombra

## 3. A projekt feltöltése GitHub-ra

A GitHub repository létrehozása után megjelenik egy oldal, ahol találod a parancsokat. Futtasd le ezeket a parancsokat a projekt könyvtárában:

```bash
cd /home/anna/lineages/használatfigyelő

# Add hozzá a GitHub remote repository-t
git remote add origin https://github.com/Ati-web/hasznalatfigyelo.git

# Vagy ha SSH-t használsz:
# git remote add origin git@github.com:Ati-web/hasznalatfigyelo.git

# Feltöltés a GitHub-ra
git push -u origin main
```

## 4. Ha a git user beállításokat módosítani szeretnéd

Ha módosítani szeretnéd a git beállításokat csak erre a repository-ra:

```bash
cd /home/anna/lineages/használatfigyelő
git config user.name "A GitHub felhasználóneved"
git config user.email "A GitHub email címed"
```

## 5. További módosítások feltöltése

Ha később módosításokat végzel a projektben, használd ezeket a parancsokat:

```bash
cd /home/anna/lineages/használatfigyelő

# Változások hozzáadása
git add .

# Commit létrehozása
git commit -m "Rövid leírás a változásokról"

# Feltöltés GitHub-ra
git push
```

## Hasznos tippek

- A `.gitignore` fájl már tartalmazza az összes felesleges fájlt (build fájlok, stb.)
- A projekt már tartalmaz README.md fájlt, ami automatikusan megjelenik a GitHub repository főoldalán
- Ha privát repository-t hoztál létre, csak te láthatod a kódot

