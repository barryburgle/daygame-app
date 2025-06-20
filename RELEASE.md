<div align="center">
<br />
<img src="app/src/main/res/mipmap-xhdpi/ic_launcher_round.webp" />
</div>

<h1 align="center">Daygame Release Guide</h1>

<br />


<div align="center">
  <img src="https://img.shields.io/github/license/barryburgle/daygame-app?style=for-the-badge" alt="License GPL-3.0" />
  <a href="https://github.com/barryburgle/daygame-app/releases/latest">
    <img src="https://img.shields.io/github/v/release/barryburgle/daygame-app?color=purple&include_prereleases&logo=github&style=for-the-badge" alt="Download from GitHub" />
  </a>
</div>

This document provides a guide to manage new versions releases.

### How to release new versions

1. Merge all the feature branches in a branch with name `vX.Y.Z` with:
   - `X` major version number
   - `Y` minor version number
   - `Z` bugfix version number
2. Update the [CHANGELOG.md](https://github.com/barryburgle/daygame-app/blob/main/CHANGELOG.md) file with all the `vX.Y.Z` features and bugfixes
3. Update the app version in the [build.gradle](https://github.com/barryburgle/daygame-app/blob/main/app/build.gradle) file to `vX.Y.Z`
4. Generate new screenshots of the new functionality and include those in the [README.md](https://github.com/barryburgle/daygame-app/blob/main/README.md) file
5. Merge the `vX.Y.Z` branch into the `develop` branch and push it
5. Merge the `develop` branch into the `main` branch and push it
6. Create a new tag on the main branch for version `vX.Y.Z` using the following command:
   ```
   git tag -a vX.Y.Z -m "vX.Y.Z" //Create the new tag
   git push origin tag vX.Y.Z //Push the new tag
   ```
7. Create a new release on Github for the latest version using the vX.Y.Z tag just created