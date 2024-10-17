!include "MUI.nsh"

Name "Desktop Finance"
# define installation directory
InstallDir "$PROGRAMFILES\Desktop Finance"
# define name of installer
OutFile "desktop-finance-installer.exe"

Var SMDir ;Start menu folder
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "LICENSE"
!insertmacro MUI_PAGE_DIRECTORY
!define MUI_ABORTWARNING
!insertmacro MUI_PAGE_STARTMENU 0 $SMDir
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_LANGUAGE "English"
 
# Admin rights are required for both install and uninstall operations
RequestExecutionLevel admin
 
# start default section
Section
 
    # set the installation directory as the destination for the following actions
    SetOutPath $INSTDIR
 
    File /r "jre"
    File "desktop-finance-nsis.exe"
    
    # create the uninstaller
    WriteUninstaller "$INSTDIR\uninstall.exe"
 
    # create a shortcut named "Desktop Finance Uninstall" in the start menu programs directory
    # point the new shortcut at the program uninstaller
    CreateShortcut "$SMPROGRAMS\Desktop Finance Uninstall" "$INSTDIR\uninstall.exe"
SectionEnd
 
# uninstaller section start
Section "uninstall"
 
    # Remove the link from the start menu
    Delete "$SMPROGRAMS\Desktop Finance Uninstall.lnk"
 
    Delete $INSTDIR\desktop-finance-nsis.exe

    RMDir /r $INSTDIR\jre
    
    # Delete the uninstaller
    Delete $INSTDIR\uninstall.exe
 
    RMDir $INSTDIR
# uninstaller section end
SectionEnd