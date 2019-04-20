import {combineReducers} from 'redux';
import directoryViewReducer from './directoryViewReducer'

export default combineReducers({
  directories: directoryViewReducer
})
