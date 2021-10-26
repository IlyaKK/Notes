# Функциональность активити с фрагментами

1. MainActivity.

* Приложение начинает основную активность в методе initialiseMenuItem(). В нём выбирается item
  навигационного меню. Если приложение запускается в первый раз, то выставляется item
  R.id.open_main_screen_item, иначе выбирется значение из bundle, которое сохраняется по ключу
  NAVIGATION_ITEM_KEY в методе onSaveInstanceState().
* Метод changerFragmentLaunch() запускает окно, в зависимости от того, какое item выставлено в меню.

2. Создание NotesListFragment.

* Если ориентация экрана портретная, то фрагмент для отображения заметок запускается в контейнере
  R.id.fragment_container_frame_layout методом launchPortraitNotesList(). Если фрагмент существует,
  то в контейнер R.id.fragment_container_frame_layout передастся существующий объект
  NotesListFragment, если нет, то создастся новый объект.
* Если альбомная, то в контейнере R.id.fragment_container_frame_layout методом
  launchLandscapeNotesList(). И, если произошёл поворот из портретной в альбомную ориентацию, при
  открытом NoteEditFragment, то вызовется fragmentManager.popBackStack(), чтобы в контейнере
  R.id.fragment_container_frame_layout показать уже существующий NotesListFragment.
* Во фрагменте NotesListFragment в initialiseTopAppBar() через интерфейс вызывается метод
  changeTopAppBar(имя фрагмента), реализованный в MainActivity, для применения ActionBar.
  Инициализируется боковое меню и появляется кнопка для открытия бокового меню, с помощью метода
  initDrawer().
* Метод fillDataFromFile() реализует чтение заметок, сохранённых в виде файлов, текст в файлах
  сохранён в формате JSON.
* Метод initialiseDeleteNoteSwipe() инициализирует коллбек для контейнера foregroundNoteCardView,
  который находится в NoteVh для анимации свайпа при удалении заметки.
* Метод initialiseNotesRecycleView() инициализирует notesRecycleView, в котором задаётся адаптер
  notesAdapter для notesRecycleView, создаётся объект для реализации анимации свайпа при удалении, в
  адаптер передаются данные для создания списка, а также адаптеру передаётся листенер, в котором
  реализовано действие при нажатии на поле заметки.
* Метод onCardClickListener вызовет в контроллере(MainActivity) метод displayNoteEdit, который
  создаст и запустит фрагмент NoteEditFragment и передаст ему объект заметки.
* Метод setNoteChange(заметка) используется для того, чтобы обновить заметку в notesRecycleView,
  передав в notesAdapter объект заметки из NoteEditFragment.

3. Создание NoteEditFragment.

* Метод displayNoteEdit() служит для создания NoteEditFragment. Он вызывается в NotesListFragment,
  но реализован в MainActivity с помощью интерфейса. Методу передаётся объект заметки, который
  сохраняется в Bundle в методе newInstance(объект заметки).
* Соответсвенно в портретной он создаётся методом launchEditNoteFragmentPortrait в контейнере
  R.id.fragment_container_frame_layout, а в альбомной launchEditNoteFragmentLandscape в контейнере
  R.id.fragment_container_2_frame_layout.
* Во фрагменте NoteEditFragment в initialiseTopAppBar() через интерфейс вызывается метод
  changeTopAppBar(имя фрагмента), реализованный в MainActivity, для применения ActionBar. Происходит
  блокирование бокового меню drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED). И в
  зависимости от ориентации экрана меняется картинка навигации.
* В методе getNoteFromNotesList() из Bundle достаётся объект заметки. Если экран был перевёрнут, то
  по ключу CHANGE_NOTE_KEY.
* В методе fillEditTexts() заполняются контейнеры данными из объекта заметки.
* В методе initialiseBackSpaceCallBack() происходит вызов реализованного в контроллере метода
  initialiseNavigationIconBack() для инициализации кнопки "назад" в верхнем левом углу экрана или
  для альбомной ориентации "галочка".
* Метод initialiseNavigationIconBack() вызывает метод saveChangeNote, когда нажимается кнопка "
  назад" в верхнем левом углу экрана или для альбомной ориентации "галочка".
* Метод changerOrientationScreen() проверяет существование фрагмента после поворота и, если
  существует, то через контроллер создаст новый фрагмент в другом контейнере, передав объект
  заметки, сохранённый в методе onSaveInstanceState(), а существующий удалится.
* Метод initialiseChangeCreateTimeDataPicker() вызывает time picker и data picker для редактирования
  времени создания заметки. Вызов происходит через правое меню экрана редактирования заметки.

4. Создание SettingsFragment и AboutFragment.

* Эти фрагменты создаются, когда нажимаются определённые item в навигационном меню. Соответственно
  они появляются в контейнере R.id.fragment_container_frame_layout в портретной ориентации и
  R.id.fragment_container_3_frame_layout в альбомной. Реализуют их вызов методы
  launchSettingsFragment() и launchAboutFragment(). В SettingsFragment можно менять режим приложения
  на ночной.