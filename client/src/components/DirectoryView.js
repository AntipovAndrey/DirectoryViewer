import React, {Component} from 'react';
import {connect} from 'react-redux';

import {collapseNode, expandNode, fetchRoot} from '../actions';
import DirectoryEntry from './DirectoryEntry';

class DirectoryView extends Component {

  async componentDidMount() {
    await this.props.fetchRoot();
    this.props.expandNode(this.props.rootDirectory)
  }

  nodeClicked = (entry) => {
    // todo: add checks
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
        <DirectoryEntry entry={root} onClick={this.nodeClicked}/>
      </ul>
    );
  }
}

const resolveChild = (node, directories) => {
  if (!node.child || node.child.length === 0) return [];
  const resolvedChild = node.child.map(child => {
    if (typeof child === 'string') return directories[child];
    return child;
  });
  resolvedChild.forEach(child => resolveChild(child, directories));
  node.child = resolvedChild
};

const mapStateToProps = ({directories}, ownProps) => {
  if (directories) {
    const rootDir = {...directories['/']};
    if (rootDir.child) {
      resolveChild(rootDir, directories)
    }
    return {rootDirectory: {...rootDir}}
  }
  return {rootDirectory: null}
};

export default connect(mapStateToProps, {fetchRoot, expandNode, collapseNode})(DirectoryView);
