"use client";

import { Suspense } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { useGLTF, OrbitControls, Html } from "@react-three/drei";
import { useScroll, useSpring, useTransform } from "framer-motion";
// 1. Import useState and useEffect from React
import { useRef, useState, useEffect } from "react";
import * as THREE from "three";
import { EffectComposer, Bloom } from "@react-three/postprocessing";

// A helper function to convert degrees to radians for THREE.js
const degToRad = (degrees) => degrees * (Math.PI / 180);

/**
 * The 3D model component.
 */
function ProductModel({ containerRef }) {
  const ROTATION_CONFIG = {
    start: { x: 0, y: 180, z: 0 },
    scroll: { x: 180, y: 360, z: 90 },
  };
  const { scene } = useGLTF("/zentap-textured.glb");
  const groupRef = useRef();

  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start start", "end end"],
  });

  const animProgress = useTransform(scrollYProgress, [0, 0.75], [0, 1], {
    clamp: true,
  });

  const smooth = useSpring(animProgress, {
    stiffness: 100,
    damping: 30,
  });

  useFrame(() => {
    if (groupRef.current) {
      const progress = smooth.get();
      groupRef.current.rotation.x =
        degToRad(ROTATION_CONFIG.start.x) +
        progress * degToRad(ROTATION_CONFIG.scroll.x);
      groupRef.current.rotation.y =
        degToRad(ROTATION_CONFIG.start.y) +
        progress * degToRad(ROTATION_CONFIG.scroll.y);
      groupRef.current.rotation.z =
        degToRad(ROTATION_CONFIG.start.z) +
        progress * degToRad(ROTATION_CONFIG.scroll.z);
    }
  });

  return <primitive ref={groupRef} object={scene} />;
}

/**
 * The main canvas component that sets up the entire 3D scene.
 */
export default function ProductCanvas({ containerRef }) {
  // 2. Add state to track if it's a touch device
  const [isTouchDevice, setIsTouchDevice] = useState(false);

  useEffect(() => {
    // 3. On component mount, check if the navigator supports touch points.
    // We check this in useEffect to ensure 'window' and 'navigator' are available.
    const checkTouch = () => {
      setIsTouchDevice(
        typeof window !== "undefined" &&
          ("ontouchstart" in window || navigator.maxTouchPoints > 0)
      );
    };
    checkTouch();
  }, []); // Empty array ensures this runs only once on mount.

  return (
    <Canvas
      className="w-full h-full"
      camera={{ position: [0, 0, 100], fov: 45 }}
      gl={{ antialias: true }}
    >
      <ambientLight intensity={0.1} />
      <directionalLight intensity={10.7} position={[40, 40, 100]} />
      <directionalLight intensity={0.8} position={[-30, 20, -50]} />
      <ProductModel containerRef={containerRef} />

      {/* 4. Pass enableRotate={!isTouchDevice} to OrbitControls.
           - On desktop (NOT touch): it will be true, allowing drag-to-rotate.
           - On mobile (IS touch): it will be false, disabling drag and allowing page scroll.
      */}
      <OrbitControls
        enableZoom={false}
        enablePan={false}
        rotateSpeed={0.5}
        enableRotate={!isTouchDevice}
      />
      <EffectComposer>
        <Bloom luminanceThreshold={0.9} intensity={0.1} mipmapBlur />
      </EffectComposer>
    </Canvas>
  );
}
