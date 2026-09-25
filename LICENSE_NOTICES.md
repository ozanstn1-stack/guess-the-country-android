# Asset and data notices

## Application source code

The Kotlin and Compose source code is released under the MIT License in `LICENSE`.

## Country data

`app/src/main/assets/countries.json` is a curated offline derivative of the country dataset published by the mledoze/countries project:

- Source: https://github.com/mledoze/countries
- Data license: Open Database License (ODbL)
- License text: https://opendatacommons.org/licenses/odbl/

The application includes country names, ISO codes, capitals, regions, currencies, and flag emoji values from that source. The file also contains editorial difficulty labels, Turkish display names, and a curated famous-place list maintained for this project. The ODbL notice applies to the database portion of the project.

## Visual assets

- `map_placeholder.xml` and `landmark_placeholder.xml` are original simplified vector illustrations created for this app.
- Launcher foreground/background vectors and splash artwork are original project artwork.
- Flag emoji are rendered by the Android system font; no third-party flag image pack is bundled.
- No copyrighted landmark photographs, map tiles, or audio recordings are included.

## Audio and haptics

Correct and incorrect answer sounds use Android's built-in `ToneGenerator`. Haptic feedback uses the platform haptics API. No external sound recordings are bundled.

If the country data is redistributed, keep the ODbL attribution and license notice with the data or its documentation.
