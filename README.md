1-# Project Structure

app/
├── manifests/
│   └── AndroidManifest.xml
├── kotlin+java/
│   └── com.khadija.bankapp/
│       ├── MainActivity.kt
│       ├── MyCanvasView
│       ├── ArabicFragment
│       ├── FrenchFragment
│       ├── LetterTracingFragment
│       ├── DrawingActivity
│       ├── LoginActivity
│       ├── RegisterActivity
│       ├── MainActivity
├── adapter/
│   ├── LettersAdapter
│   └── ViewPagerAdapter
│
├── model/
│   └── Letter
│
├── view/
│   └── MyCanvasView
│
│
├── assets/
│   ├── letters_ar.json
│   └── letters_fr.json
│
└── res/
    ├── drawable/
    └── layout/
        ├── activity_drawing.xml
        ├── activity_letters.xml
        ├── activity_main.xml
        ├── activity_register.xml
        ├── activity_selection.xml
        ├── item_letter.xml
        └── activity_login.xml

2-# Technologies Used

- Kotlin
- Android Studio
- RecyclerView (display alphabet letters)
- Canvas (drawing letters with finger)
- Room Database (save child progress)
- JSON (load letters data offline)
- MVVM Architecture
- MediaPlayer (play letter sounds)
- Material Design Components

3-# Features

- Learn Arabic Alphabet
- Learn French Alphabet
- Play sound for each letter
- Trace letters using finger on screen
- Repeat sound button
- Clear and redraw letters
- Child‑friendly interface with large icons and simple colors
- Offline access (data loaded from JSON)
- Save child progress using Room database
- Responsive UI for phones and tablets
