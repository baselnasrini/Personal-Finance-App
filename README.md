
# Personal-Finance-App
An application that will serve as a tool for organizing your personal finances. The user should be able to enter expenditures and income using the UI. The UI will also show a list of the income and expenditures within a specified time period. These results must be properly presented to the user.

## Goal
You must show a basic understanding of the development process for a simple Android application. You must also show that you understand the different components available in the Android system, and how they can cooperate with each other.
The student should:
- Show understanding of the basic components of the Android system.
- Show understanding of the life cycles of Android.
- Show understanding of different storage options available in the Android system.

### Requirements for a Godkänt
In order to achieve a grade Godkänt in this task:
1. In your application, you should use at least two fragments showing the UI (User Interface). The application may include any number of Activities. Activities may display the UI.
2. In your application, you should handle the user data (SharedPreferences).
3. In your application, you should work with several forms for entering data. Every form must have a good design, so that the UI provides a good user experience (UX). Income and expenditure shall be saved in the database.
4. In your application, you should present a summary of the user's financial situation.
5. In your application, the user should be able to see all the expenses in a clickable listview, and all income in another clickable listview. The UI should offer the user the possibility to toggle between expenditure and income. All stored data item should appear when requested.
6. The user can choose to see all income between two dates in a clickable listview.
7. Use SQLite to save both expenses and income.
8. Your application must be persistent. The UI data must be preserved after any screen rotation or moving background. This means that you must ensure that the application also works under this conditions. Add any backup files if needed.

### Requirements for a Väl Godkänt
In order to achieve a grade Väl Godkänt, these features must also be implemented:
1. UI classes must be implemented as Fragments containing no logic but handling events.
2. Display expenses and income graphically with any kind of chart: pie charts, bar graphs, etc. You are free to look for and use any library that helps you accomplish this.
3. Use the camera to scan barcodes and/or receipts for automatic entry of expenditure. This requires you to create a table to relate barcodes to categories, titles, dates, and amounts. The user will indicate the category, title and price for each new barcode scanned. It is strongly encouraged to use existing applications for barcode reading for this task.
4. At least one inherited View, e.g. extending TextView or Button, will be used in the application.
5. The application shall include the dynamic change of fragments.

<img src="https://user-images.githubusercontent.com/26624976/86121541-73652e80-bad6-11ea-94db-f180c285a2e4.png" width="400" height="800" />
<img src="https://user-images.githubusercontent.com/26624976/86121542-73652e80-bad6-11ea-95b1-3eaf45856dd4.png" width="400" height="800" />
<img src="https://user-images.githubusercontent.com/26624976/86121539-72340180-bad6-11ea-8a74-059fae514400.png" width="400" height="800" />
<img src="https://user-images.githubusercontent.com/26624976/86121540-72cc9800-bad6-11ea-9c8e-dfbf8291e5d8.png" width="400" height="800" />
