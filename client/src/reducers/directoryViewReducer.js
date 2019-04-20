import {COLLAPSE_NODE, EXPAND_NODE, FETCH_ROOT} from '../actions/types';

export default (state = null, action) => {
  switch (action.type) {
    case FETCH_ROOT:
      return {...action.payload};
    case EXPAND_NODE:
      return {...state}; //todo: find clicked node, add child
    case COLLAPSE_NODE:
      return {...state}; //todo: find clicked node, remove child
    default:
      return state;
  }
};
