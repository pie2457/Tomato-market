import AddressModal from "@components/Modal/AddressModal/AddressModal";
import CategoryModal from "@components/Modal/CategoryModal/CategoryModal";
import GlobalStyle from "@styles/GlobalStyle";
import { theme } from "@styles/designSystem";
import styled, { ThemeProvider } from "styled-components";

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Dim>
        <CategoryModal />
        <AddressModal />
      </Dim>
    </ThemeProvider>
  );
}

const Dim = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  gap: 16px;
  background-color: ${({ theme: { color } }) => color.neutralOverlay};
`;
