import {COLLAPSE_NODE, EXPAND_NODE, FETCH_ROOT} from './types';
import directoryApi from '../api/directoryapi';

export const fetchRoot = () => async dispatch => {
  const res = await directoryApi.get('/root');
  dispatch({type: FETCH_ROOT, payload: res.data})
};

export const expandNode = node => async dispatch => {
  const uriPath = `${node.directoryPathComponents.join('/')}/${node.name}`;
  const res = await directoryApi.get(`/view${uriPath}`);
  dispatch({
    type: EXPAND_NODE,
    payload: {
      child: res.data,
      node
    }
  })
};

export const collapseNode = node => ({
  type: COLLAPSE_NODE,
  payload: node
});
