export const calculateNodeId = directoryNode => {
  const dir = directoryNode.directoryPathComponents.map(c => `${c}/`).join('');
  return `/${dir}${directoryNode.name}`
};
