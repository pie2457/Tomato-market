import React from "react";
import styled from "styled-components";
import ModalHeader, { ModalHeaderProps } from "./ModalHeader";

type ModalProps = {
  headerProps: ModalHeaderProps;
  content: React.ReactNode;
};

export default function Modal({ headerProps, content }: ModalProps) {
  return (
    <StyledModal>
      <ModalHeader {...headerProps} />
      <ModalContent>{content}</ModalContent>
    </StyledModal>
  );
}

const StyledModal = styled.div`
  width: 320px;
  height: 700px;
  background-color: ${({ theme: { color } }) => color.neutralBackground};
  border-radius: ${({ theme: { radius } }) => radius[16]};
`;

const ModalContent = styled.main`
  min-height: 264px;
  max-height: 600px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0px 24px;
`;
