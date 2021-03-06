cchlib-tools-duplicatefiles

---------------------------------------------------------------------
4.1.7 (Java 1.7 required)
  Add context menu on left file list. Sort and select display file
  Many improvement on UI refresh
  Add Drag and Drop support
---------------------------------------------------------------------
4.1.6
  Add support of some Java 1.7 features.
  Generated target code now is Java7
---------------------------------------------------------------------
4.1.5
  Fix: some problems with resources (since maven migration)
  Use: cx.ath.choisnet.swing.XTextField (context menu copy/paste)
  Fix: Concurrent access problem while deleting files
---------------------------------------------------------------------
4.1.2
  Moving 2 maven (remove ant build number)
---------------------------------------------------------------------
04.00 Maven version
---------------------------------------------------------------------
03a.80
  Migrate to maven
---------------------------------------------------------------------
03a.21
  Add possibility to scan only some files
---------------------------------------------------------------------
03a.20
  Move release note to package.
  Fix: Cancel during scan.
---------------------------------------------------------------------

---------------------------------------------------------------------
Known problems:
  - After Cancel should not can use "continue" button.
  - [Advanced/Expert Mode] Select using regex : Buttons 'delete' and
    'keep' (panel 'duplicates') should be disabled during process.
    -> try to make a better interface.
---------------------------------------------------------------------
To improve:
  - Compare only some files (advanced or expert only)
  - Ignore some folders :
    a) Before compare
    b) After compare (context menu, add ignore all files in) - should
    remove from list also files that not have duplicate any more.
  - Bypass compressed files and recycle bin option
---------------------------------------------------------------------
To add:
 - dans la fenetre de suppression des fichiers
   * Selection multiple
   * Menu delete all other
 - Update duplicate (if removed using other application)
 - When canceling go to duplicates
 - Preferences (create a folder under userhome .duplicatefiles)
   - Save files filters, user level, looknfeel, regexp, ...
 - Change default for scan (include hidden folders and hidden files)
 - Ultra Fast filename only scan option
 - MP3 Support:
    Search for Duplicate Songs (ID3 Tag Comparison)
    Scan your computer for duplicate music
    Compare by: Song Title, Artist, Album, and more!
 - Save scan results to a file and re-import later for further
   analysis and resolution
 - Protect critical system files and folders
 - Easily rename selected duplicates to test which files are safe
   to delete (must be able to restore previous version)
 - Rename duplicate files with prefix/postfix
 - Save program state on exit and re-entrance
 - Move duplicate files to folder
 - Show file properties
 - Open file with associated program
 - Improve filter:
   + Add editor for advance users able to chooser order of
     filters, type of filter (on name, on path, on properties /
     begin, end, regexp,...). Also able to do some OR or AND
 operation on filters.
---------------------------------------------------------------------
Nice to have:
 - Move files to Recycle Bin or delete duplicate files permanently
 - Duplicates Report Export
    Generate an HTML fully hyper-linked report
    Export duplicates report to a TXT file
    Export a report of duplicates to Excel in CSV format

---------------------------------------------------------------------
Future extensions:
  - Ability to generate scripts (instead of delete) [on the last panel]
  - Ability to store (all) hash codes (filename, last modified,
    the hash code) so you can reuse it later. Will be used as cache
    or look for these files on different computers (without sharing).
---------------------------------------------------------------------
