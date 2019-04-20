import {COLLAPSE_NODE, EXPAND_NODE, FETCH_ROOT} from '../actions/types';
import {calculateNodeId} from '../utils/nodes';
import _ from 'lodash';

export default (state = null, action) => {
  switch (action.type) {
    case FETCH_ROOT:
      return {'/': action.payload};
    case EXPAND_NODE:
      const descendants = _.mapKeys(action.payload.child, calculateNodeId);
      const expandNodeId = calculateNodeId(action.payload.node);
      const newExpandedState = {
        ...state, ...descendants
      };
      const expandedNode = newExpandedState[expandNodeId];
      expandedNode.child = Object.keys(descendants);
      expandedNode.expanded = true;
      return newExpandedState;
    case COLLAPSE_NODE:
      const newCollapsedState = {...state};
      const collapseNodeId = calculateNodeId(action.payload);
      const elementToCollapse = newCollapsedState[collapseNodeId];
      if (elementToCollapse.child) {
        elementToCollapse.child.forEach(childId => {
          delete newCollapsedState[childId]
        });
        elementToCollapse.child = null;
      }
      elementToCollapse.expanded = false;
      return newCollapsedState;
    default:
      return state;
  }
};
