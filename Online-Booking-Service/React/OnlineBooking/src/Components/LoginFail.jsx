import React, { Component } from 'react';
import lottie from 'lottie-web';
import animated from "../assets/fail.json";

class LoginFail extends Component {
  constructor(props) {
    super(props);
    this.containerRef = React.createRef();
  }

  componentDidMount() {
    this.animation = lottie.loadAnimation({
      container: this.containerRef.current,
      renderer: 'svg',
      loop: true,
      autoplay: true,
      animationData: animated, // Your JSON animation data
    });
  }

  componentWillUnmount() {
    // Cleanup code if needed
    this.animation.destroy();
  }

  render() {
    return <div className='w-2/6 mt-24 ml-44' ref={this.containerRef}></div>;
  }
}

export default LoginFail;
