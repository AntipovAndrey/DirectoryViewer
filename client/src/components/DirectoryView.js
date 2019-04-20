import React, {Component} from 'react';
import {connect} from 'react-redux';
import _ from 'lodash';

import {collapseNode, expandNode, fetchRoot} from '../actions';
import DirectoryEntry from './DirectoryEntry';
import {calculateNodeId} from "../utils/nodes";

class DirectoryView extends Component {

  async componentDidMount() {
    if (!this.props.rootDirectory) {
      await this.props.fetchRoot();
      this.props.expandNode(this.props.rootDirectory);
    }
  }

  nodeClicked = (entry) => {
    if (!entry.metaData.expandable) {
      return
    }
    if (!entry.metaData.expandSupported) {
      //todo: dialog
      return
    }
    if (entry.expanded) {
      this.props.collapseNode(entry)
    } else {
      this.props.expandNode(entry)
    }
  };

  render() {
    const root = this.props.rootDirectory;

    if (!root) {
      return <div>loading</div>
    }

    return (
      <ul>
        <DirectoryEntry entry={root}
                        keyProvider={node => calculateNodeId(node)}
                        onClick={this.nodeClicked}/>
      </ul>
    );
  }
}

const denormalizeChild = (node, directories) => {
  if (!node.child || node.child.length === 0) return [];
  const resolvedChild = node.child.map(child => {
    if (typeof child === 'string') return directories[child];
    return child;
  });
  resolvedChild.forEach(child => denormalizeChild(child, directories));
  node.child = _.sortBy(resolvedChild, 'name')
};

const mapStateToProps = ({directories}, ownProps) => {
  if (directories) {
    const rootDir = {...directories['/']};
    if (rootDir.child) {
      denormalizeChild(rootDir, directories)
    }
    return {rootDirectory: {...rootDir}}
  }
  return {rootDirectory: null}
};

export default connect(mapStateToProps, {fetchRoot, expandNode, collapseNode})(DirectoryView);
