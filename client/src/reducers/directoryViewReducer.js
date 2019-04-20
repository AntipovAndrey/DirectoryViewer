import {COLLAPSE_NODE, EXPAND_NODE, FETCH_ROOT} from '../actions/types';
import _ from 'lodash';

const calculatePath = directoryNode => {
  console.log(directoryNode);
  const dir = directoryNode.directoryPathComponents.join('/');
  return `${dir}/${directoryNode.name}`
};

export default (state = null, action) => {
  switch (action.type) {
    case FETCH_ROOT:
      return {'/': action.payload};
    case EXPAND_NODE:
      const descendants = _.mapKeys(action.payload.child, calculatePath);
      const expandNodeId = calculatePath(action.payload.node);
      const newEspandedState = {
        ...state, ...descendants
      };
      newEspandedState[expandNodeId].child = Object.keys(descendants);
      return newEspandedState;
    case COLLAPSE_NODE:
      const newCollapsedState = {...state};
      const collapseNodeId = calculatePath(action.payload);
      const elementToCollapse = newCollapsedState[collapseNodeId];
      if (elementToCollapse.child) {
        elementToCollapse.child.forEach(childId => {
          delete newCollapsedState[childId]
        });
        elementToCollapse.child = null;
      }
      return newCollapsedState;
    default:
      return state;
  }
};
