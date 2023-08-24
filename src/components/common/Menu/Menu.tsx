import { styled } from "styled-components";
import { designSystem } from "@styles/designSystem";
import MenuItem, { ItemProps } from "./MenuItem";

interface MenuProps extends React.ComponentPropsWithoutRef<"ul"> {
  itemList: ItemProps[];
  withShadow?: boolean;
  position?: "left" | "right";
}

export default function Menu({ itemList, withShadow, position }: MenuProps) {
  return (
    <StyledMenu $withShadow={withShadow} $position={position}>
      {itemList.map((item) => (
        <MenuItem key={item.itemId} item={item} />
      ))}
    </StyledMenu>
  );
}

const StyledMenu = styled.ul<{
  $withShadow?: boolean;
  $position?: "left" | "right";
}>`
  width: 240px;
  position: absolute;
  top: 56px;
  ${(props) => (props.$position === "left" ? `left: 16px` : "right: 256px")};
  background-color: ${designSystem.color.neutralBackground};
  border: 0.8px solid ${designSystem.color.neutralBorder};
  border-radius: 12px;
  box-shadow: ${(props) =>
    props.$withShadow ? "0px 4px 4px 0px #00000040" : "none"};
  overflow: hidden;
  z-index: 1;
`;
