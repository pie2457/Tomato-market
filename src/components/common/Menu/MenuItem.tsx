import { styled } from "styled-components";
import { designSystem } from "@styles/designSystem";

export type ItemProps = {
  itemId: number;
  value: string;
  isSelected?: boolean;
  isWarning?: boolean;
  onClickWithId?: (id: number) => void;
  onClick?: () => void;
};

export default function MenuItem({ item }: { item: ItemProps }) {
  const onItemClick = () => {
    if (item.onClickWithId) {
      item.onClickWithId(item.itemId);
      return;
    }

    item.onClick?.();
  };

  return (
    <StyledItem
      $isSelected={item.isSelected}
      $isWarning={item.isWarning}
      onClick={onItemClick}>
      {item.value}
    </StyledItem>
  );
}

const StyledItem = styled.li<{
  $isSelected?: boolean;
  $isWarning?: boolean;
}>`
  color: ${(props) =>
    props.$isWarning
      ? designSystem.color.systemWarning
      : designSystem.color.neutralTextStrong};
  background-color: inherit;
  font: ${(props) =>
    props.$isSelected
      ? designSystem.font.enabledStrong16
      : designSystem.font.availableDefault16};
  border-bottom: 0.8px solid ${designSystem.color.neutralBorder};
  padding: 16px;
  cursor: pointer;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    opacity: ${designSystem.opacity.press};
  }
`;
