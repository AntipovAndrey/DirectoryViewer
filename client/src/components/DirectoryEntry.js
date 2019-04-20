import React from 'react';

function renderChild(entry, onClick) {
  if (!entry.child || entry.child.length === 0) {
    return null
  }
  return (
    <ul style={{paddingLeft: 20}}>
      {entry.child.map(c => <DirectoryEntry entry={c} onClick={onClick}/>)}
    </ul>
  );
}

// todo: refactor + fix appearance
const DirectoryEntry = ({entry, onClick}) => {

  const canOpen = entry.metaData.expandable && entry.metaData.expandSupported;

  const style = {
    color: canOpen ? 'green' : 'black'
  };

  return (
    <li style={style} onClick={(e) => {
      e.stopPropagation();
      onClick(entry);
    }}>
      {entry.expanded ? '-' : '+'} {entry.name} {entry.metaData.type}
      {renderChild(entry, onClick)}
    </li>
  );
};

export default DirectoryEntry;