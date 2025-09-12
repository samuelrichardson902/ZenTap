"use client";

import { Canvas, useFrame } from "@react-three/fiber";
import { useGLTF, OrbitControls, Environment } from "@react-three/drei";
import { useScroll, useSpring } from "framer-motion";
import { useRef, useEffect } from "react";
import * as THREE from "three";
import { EffectComposer, Bloom } from "@react-three/postprocessing";

function ProductModel() {
  // Load the model
  const { scene } = useGLTF("/zentap-optimized.glb");
  const groupRef = useRef();
  
  // Apply material properties and center the model
  useEffect(() => {
    // Center the model's geometry
    const box = new THREE.Box3().setFromObject(scene);
    const center = box.getCenter(new THREE.Vector3());
    scene.position.sub(center);

    // Traverse the scene to find meshes and update their materials
    scene.traverse((child) => {
      if (child.isMesh) {
        // To make the material metallic, we adjust metalness and roughness
        // metalness: 1.0 makes it fully metallic.
        // roughness: 0.4 makes it semi-reflective, not a perfect mirror.
        child.material.metalness = 0.7;
        child.material.roughness = 0.6;
      }
    });
  }, [scene]);

  // Framer Motion scroll animation
  const { scrollYProgress } = useScroll();
  const smooth = useSpring(scrollYProgress.get(), { stiffness: 100, damping: 30 });

  useFrame(() => {
    // Rotate the model group based on scroll progress
    if (groupRef.current) {
      groupRef.current.rotation.y = smooth.get() * Math.PI * 2;
    }
  });

  return <primitive ref={groupRef} object={scene} />;
}

export default function ProductCanvas() {
  return (
    <Canvas 
      className="w-full h-full" 
      camera={{ position: [0, 0, 100], fov: 45 }}
      gl={{ antialias: true }}
    >
      {/* 1. LIGHTING & ENVIRONMENT */}
      {/* Reduced ambient light to avoid washing out the scene */}
      <ambientLight intensity={0.1} />
      
      {/* A strong spotlight to create highlights and shadows */}
      <spotLight 
        position={[2550, 250, 100]} 
        angle={0.35} 
        penumbra={1} 
        intensity={1} 
        castShadow 
      />
      
      {/* The Environment component adds realistic lighting and reflections from an HDRI map */}
      {/* The 'city' preset is a good starting point for neutral studio lighting. */}
      <Environment preset="studio" />

      {/* 2. THE MODEL */}
      <ProductModel />
      
      {/* Controls for user interaction */}
      <OrbitControls
        enableZoom={false}
        enablePan={false}
        rotateSpeed={0.5}
      />

      {/* 3. POST-PROCESSING EFFECTS */}
      <EffectComposer>
        {/* Bloom adds a glow effect to bright areas, giving it a premium feel. */}
        <Bloom 
          luminanceThreshold={0.6} // How bright a pixel needs to be to bloom
          intensity={0.15}          // Strength of the glow
          mipmapBlur              // Creates a more natural, feathered blur
        />
      </EffectComposer>
    </Canvas>
  );
}