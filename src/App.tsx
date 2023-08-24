import { ReactComponent as ChevronDownIcon } from "@assets/icon/chevron-down.svg";
import { ReactComponent as CircleXIcon } from "@assets/icon/circle-x-filled.svg";
import { ReactComponent as HomeIcon } from "@assets/icon/home.svg";
import { ReactComponent as LayoutGridIcon } from "@assets/icon/layout-grid.svg";
import { ReactComponent as PlusIcon } from "@assets/icon/plus.svg";
import Button from "@components/common/Button";
import GlobalStyle from "@styles/GlobalStyle";
import { theme } from "@styles/designSystem";
import styled, { ThemeProvider } from "styled-components";

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Button
        fontName="availableStrong16"
        rightIcon={<ChevronDownIcon />}
        value="역삼1동"
      />
      <Button
        size={{ width: "40px", height: "40px" }}
        leftIcon={<LayoutGridIcon />}
      />
      <Button
        size={{ width: "48px", height: "48px" }}
        color="neutralTextWeak"
        direction="column"
        fontName="enabledStrong10"
        leftIcon={<HomeIcon />}
        value="홈화면"
      />
      <Button
        size={{ width: "56px", height: "56px" }}
        leftIcon={<PlusIcon />}
        color="accentText"
        backgroundColor="accentPrimary"
        radius="half"
      />
      <Button
        size={{ width: "288px", height: "56px" }}
        leftIcon={<PlusIcon />}
        fontName="availableStrong16"
        color="accentTextWeak"
        borderColor="neutralBorder"
        radius={8}
        value="추가"
      />
      <AddressSelector onClick={() => console.log("선택한 동네 변경")}>
        <span>역삼1동</span>
        <Button
          leftIcon={<CircleXIcon />}
          color="accentText"
          onClick={(e) => {
            e.stopPropagation();
            console.log("동네 삭제");
          }}
        />
      </AddressSelector>
      <Button color="accentTextWeak" fontName="displayDefault16" value="취소" />
      <Button color="systemWarning" fontName="displayStrong16" value="삭제" />
    </ThemeProvider>
  );
}

const AddressSelector = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 0 16px;
  align-items: center;
  width: 288px;
  height: 56px;
  cursor: pointer;

  background-color: ${({ theme: { color } }) => color.accentPrimary};
  border-radius: ${({ theme: { radius } }) => radius[8]};
  font: ${({ theme: { font } }) => font.availableStrong16};
  color: ${({ theme: { color } }) => color.accentText};
`;
