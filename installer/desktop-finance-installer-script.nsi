!include "MUI.nsh"
!include "nsDialogs.nsh"
!include "LogicLib.nsh"

!define ProductName "Desktop Finance v0.1"
!define ExecutableFileName "desktop-finance-nsis"

Name "${ProductName}"
# define installation directory
InstallDir "$PROGRAMFILES\Desktop Finance"
# define name of installer
OutFile "desktop-finance-installer.exe"

Var Dialog
Var Start_Menu_Shortcut_Checkbox
Var Start_Menu_Shortcut_Checkbox_State
Var Desktop_Shortcut_Checkbox
Var Desktop_Shortcut_Checkbox_State

!insertmacro MUI_PAGE_WELCOME
!define MUI_LICENSEPAGE_CHECKBOX
!define MUI_LICENSEPAGE_CHECKBOX_TEXT "I accept the terms in the License Agreement"
!insertmacro MUI_PAGE_LICENSE "LICENSE"
!insertmacro MUI_PAGE_DIRECTORY
!define MUI_ABORTWARNING
Page custom CustomShortcuts CustomShortcutsLeave
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!define MUI_UNABORTWARNING
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

!insertmacro MUI_LANGUAGE "English"

# Admin rights are required for both install and uninstall operations
RequestExecutionLevel admin

# Reference: https://nsis.sourceforge.io/Docs/nsDialogs/Readme.html
Function CustomShortcuts

    !insertmacro MUI_HEADER_TEXT "Shortcuts" "Select shortcuts to create."

    nsDialogs::Create 1018
	Pop $Dialog

	${If} $Dialog == error
		Abort
	${EndIf}

    ${NSD_CreateCheckbox} 0 70u 100% 10u "&Create desktop shortcut(s)"
	Pop $Desktop_Shortcut_Checkbox

    ${NSD_Check} $Desktop_Shortcut_Checkbox

	${NSD_CreateCheckbox} 0 100u 100% 10u "&Create start menu shortcut(s)"
	Pop $Start_Menu_Shortcut_Checkbox

    ${NSD_Check} $Start_Menu_Shortcut_Checkbox

	nsDialogs::Show
FunctionEnd

Function CustomShortcutsLeave
    ${NSD_GetState} $Desktop_Shortcut_Checkbox $Desktop_Shortcut_Checkbox_State
    ${NSD_GetState} $Start_Menu_Shortcut_Checkbox $Start_Menu_Shortcut_Checkbox_State
FunctionEnd

Section

    # Set the installation directory as the destination for the following actions
    SetOutPath $INSTDIR

    File /r "jre"
    File "${ExecutableFileName}.exe"

    # Create a desktop shortcut if the user selected the option
    ${If} $Desktop_Shortcut_Checkbox_State == ${BST_CHECKED}
        CreateShortCut "$DESKTOP\${ProductName}.lnk" "$INSTDIR\${ExecutableFileName}.exe" ""
    ${EndIf}


    # Create a start menu shortcut if the user selected the option
    ${If} $Start_Menu_Shortcut_Checkbox_State == ${BST_CHECKED}
        CreateDirectory "$SMPROGRAMS\${ProductName}"
        CreateShortCut "$SMPROGRAMS\${ProductName}\${ProductName}.lnk" "$INSTDIR\${ExecutableFileName}.exe" "" "$INSTDIR\${ExecutableFileName}.exe" 0
    ${EndIf}

    # Write uninstall information to the registry
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ProductName}" "DisplayName" "${ProductName} (remove only)"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ProductName}" "UninstallString" "$INSTDIR\uninstall.exe"

    # Create the uninstaller
    WriteUninstaller "$INSTDIR\uninstall.exe"

SectionEnd

Section "uninstall"

    # Remove the desktop shortcut
    Delete "$DESKTOP\${ProductName}.lnk"

    # Remove the start menu shortcut
    Delete "$SMPROGRAMS\${ProductName}\${ProductName}.lnk"
    Delete "$SMPROGRAMS\${ProductName}\*.*"
    RMDir "$SMPROGRAMS\${ProductName}"

    # Remove the EXE file and the JRE folder
    Delete $INSTDIR\desktop-finance-nsis.exe
    RMDir /r $INSTDIR\jre

    # Delete Unistall Registry Entries
    DeleteRegKey HKEY_LOCAL_MACHINE "SOFTWARE\${ProductName}"
    DeleteRegKey HKEY_LOCAL_MACHINE "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\${ProductName}"

    # Delete the uninstaller
    Delete $INSTDIR\uninstall.exe

    RMDir $INSTDIR
SectionEnd