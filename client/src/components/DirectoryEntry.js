import React from 'react';
import './DirectoryEntry.css'

const getIcon = entry => {

  let icon;
  switch (entry.metaData.type.toLowerCase()) {
    case 'directory':
      if (entry.expanded) {
        icon = 'folder_open';
      } else {
        icon = 'folder';
      }
      break;
    case 'archive':
      icon = 'archive';
      break;
    case 'image':
      icon = 'photo';
      break;
    case 'music':
      icon = 'music_note';
      break;
    case 'video':
      icon = 'videocam';
      break;
    case 'sourcecode':
      icon = 'code';
      break;
    case 'executable':
      icon = 'play_arrow';
      break;
    default:
      icon = 'insert_drive_file'
  }

  return <i style={{fontSize: '1em'}} className="material-icons">{icon}</i>;
};

const renderChild = (entry, onClick, keyProvider) => {
  if (!entry.child || entry.child.length === 0) {
    return null
  }
  return (
    <ul>
      {entry.child.map(c =>
        <DirectoryEntry entry={c}
                        key={keyProvider(c)}
                        onClick={onClick}
                        keyProvider={keyProvider}/>)}
    </ul>
  );
};

const DirectoryEntry = ({entry, onClick, keyProvider}) => {
  const entryClicked = e => {
    e.stopPropagation();
    onClick(entry);
  };
  return (
    <li>
      <p onClick={entryClicked}>{getIcon(entry)} {entry.name}</p>
      {renderChild(entry, onClick, keyProvider)}
    </li>
  );
};

export default DirectoryEntry;