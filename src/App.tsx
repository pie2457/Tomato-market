import GlobalStyle from "@styles/GlobalStyle";
import designSystem from "@styles/designSystem";
import styled, { ThemeProvider } from "styled-components";

export default function App() {
  return (
    <ThemeProvider theme={designSystem}>
      <GlobalStyle />
      <main>wow</main>
      <Button>스타일 리셋 됐니?</Button>
      <input />
    </ThemeProvider>
  );
}

const Button = styled.button`
  font: ${({ theme: { font } }) => font.displayStrong20};
  color: ${({ theme: { color } }) => color.accent.secondary};
  background-color: ${({ theme: { color } }) => color.accent.primary};
`;
