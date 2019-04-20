import React, {Component} from 'react';
import {connect} from 'react-redux';

import {collapseNode, expandNode, fetchRoot} from '../actions';

class DirectoryView extends Component {

  componentDidMount() {
    this.props.fetchRoot()
  }

  render() {
    return (
      <div>
        <p>{JSON.stringify(this.props.directories)}</p>
        <a href={"#"} onClick={() => this.props.expandNode(this.props.directories['/'])}>expand root</a>
        <br/>
        <a href={"#"} onClick={() => this.props.collapseNode(this.props.directories['/'])}>collapse root</a>
      </div>
    );
  }
}

const mapStateToProps = ({directories}) => {
  return {directories}
};

export default connect(mapStateToProps, {fetchRoot, expandNode, collapseNode})(DirectoryView);
