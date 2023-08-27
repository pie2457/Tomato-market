import { MutableRefObject, useEffect, useRef, useState } from "react";

type DropdownReturnType = {
  isOpen: boolean;
  ref: MutableRefObject<HTMLDivElement | null>;
  toggleOpenState: () => void;
};

export default function useDropdown(): DropdownReturnType {
  const [isOpen, setIsOpen] = useState(false);
  const ref = useRef<HTMLDivElement | null>(null);

  const toggleOpenState = () => {
    setIsOpen((prev) => !prev);
  };

  useEffect(() => {
    const onOutsideClick = (e: MouseEvent) => {
      if (!ref.current?.contains(e.target as Node)) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      window.addEventListener("click", onOutsideClick);
    }

    return () => {
      window.removeEventListener("click", onOutsideClick);
    };
  }, [isOpen]);

  return {
    isOpen,
    ref,
    toggleOpenState,
  };
}
