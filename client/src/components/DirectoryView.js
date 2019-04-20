import React, {Component} from 'react';
import {connect} from 'react-redux';

import {fetchRoot} from '../actions';

class DirectoryView extends Component {

  componentDidMount() {
    this.props.fetchRoot()
  }

  render() {
    return (
      <div>{JSON.stringify(this.props.directories)}</div>
    );
  }
}

const mapStateToProps = ({directories}) => {
  return {directories}
};

export default connect(mapStateToProps, {fetchRoot})(DirectoryView);
