import useDropdown from "@hooks/useDropdown";
import { ReactNode } from "react";
import { styled } from "styled-components";
import Menu from "./Menu";
import { ItemProps } from "./MenuItem";

type MenuIndicatorProps = React.HTMLAttributes<HTMLDivElement> & {
  children: ReactNode;
  itemList: ItemProps[];
  withShadow?: boolean;
  position?: "left" | "right";
};

export default function MenuIndicator({
  children,
  itemList,
  withShadow,
  position,
}: MenuIndicatorProps) {
  const { isOpen, ref, toggleOpenState } = useDropdown();

  return (
    <>
      <StyledMenuIndicator ref={ref} onClick={toggleOpenState}>
        {children}
        {isOpen && (
          <Menu
            itemList={itemList}
            withShadow={withShadow}
            position={position}
          />
        )}
      </StyledMenuIndicator>
    </>
  );
}

const StyledMenuIndicator = styled.div`
  position: relative;
`;
