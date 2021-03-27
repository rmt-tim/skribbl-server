# Vrste poruka
## 💻 Poruke koje šalje klijent
### Login
```json
{
  "tip": "login",
  "username": "jovan"
}
```

### Pokreni igru
```json
{
  "tip": "pokreni_igru"
}
```

### Izbor reči
```json
{
  "tip": "izbor",
  "rec": "paradajz"
}
```

### Crtanje
```json
{
  "tip": "crtanje",
  ...
}
```

### Pogađanje
```json
{
  "tip": "pogadjanje",
  "rec": "konj"
}
```

## 🤖 Poruke koje šalje server
### Lista igrača
```json
{
  "tip": "lista_igraca",
  "lista_igraca": [ "teodora", "jovan", "janko" ]
}
```

### Igra je pokrenuta
```json
{
  "tip": "igra_pokrenuta"
}
```

### Ponuda izbora reči
```json
{
  "tip": "ponudjen_izbor",
  "ponudjene_reci": [ "paradajz", "telefon", "vrata" ]
}
```

### Reč odabrana
```json
{
  "tip": "odabrana_rec",
  "rec": "paradajz"
}
```

### Crtanje
```json
{
  "tip": "crtanje",
  ...
}
```

### Chat
```json
{
  "tip": "chat",
  "poruka": "teodora je pogodio/la rec!"
}
```

### Kraj runde
Polje `"nacin"` je `"vreme_isteklo"` ili `"svi_pogodili"`.
```json
{
  "tip": "kraj_runde",
  "nacin": "vreme_isteklo",
  "poeni": [
    { "teodora": 300 },
    { "jovan": 250 },
    { "janko": 200 }
  ]
}
```

### pocetno stanje?
