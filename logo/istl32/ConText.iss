;This file will be executed next to the application bundle image
;I.e. current directory will contain folder ConText with application files
[Setup]
AppId={{fxApplication}}
AppName=ConText
AppVersion=1.2.0-x86
AppVerName=ConText-1.2.0-x86
AppPublisher=University of Illinois
AppComments=Context-FX
AppCopyright=
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={pf}\ConText
DisableStartupPrompt=Yes
DisableDirPage=No
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=University of Illinois
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=ConText-1.2.0-x86
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=ConText\ConText.ico
UninstallDisplayIcon={app}\ConText.ico
UninstallDisplayName=ConText
WizardImageStretch=No
WizardSmallImageFile=ConText-setup-icon.bmp   

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "ConText\ConText.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "ConText\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\ConText"; Filename: "{app}\ConText.exe"; IconFilename: "{app}\ConText.ico"; Check: returnTrue()
Name: "{commondesktop}\ConText"; Filename: "{app}\ConText.exe";  IconFilename: "{app}\ConText.ico"; Check: returnFalse()

[Run]
Filename: "{app}\ConText.exe"; Description: "{cm:LaunchProgram,ConText}"; Flags: nowait postinstall skipifsilent

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
