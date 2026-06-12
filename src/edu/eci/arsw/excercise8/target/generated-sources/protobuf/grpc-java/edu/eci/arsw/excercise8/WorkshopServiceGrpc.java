package edu.eci.arsw.excercise8;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: eciciencia.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class WorkshopServiceGrpc {

  private WorkshopServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "WorkshopService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ReserveRequest,
      edu.eci.arsw.excercise8.ReserveResponse> getReserveSpotMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReserveSpot",
      requestType = edu.eci.arsw.excercise8.ReserveRequest.class,
      responseType = edu.eci.arsw.excercise8.ReserveResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ReserveRequest,
      edu.eci.arsw.excercise8.ReserveResponse> getReserveSpotMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ReserveRequest, edu.eci.arsw.excercise8.ReserveResponse> getReserveSpotMethod;
    if ((getReserveSpotMethod = WorkshopServiceGrpc.getReserveSpotMethod) == null) {
      synchronized (WorkshopServiceGrpc.class) {
        if ((getReserveSpotMethod = WorkshopServiceGrpc.getReserveSpotMethod) == null) {
          WorkshopServiceGrpc.getReserveSpotMethod = getReserveSpotMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise8.ReserveRequest, edu.eci.arsw.excercise8.ReserveResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReserveSpot"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ReserveRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ReserveResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WorkshopServiceMethodDescriptorSupplier("ReserveSpot"))
              .build();
        }
      }
    }
    return getReserveSpotMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.CancelReserveRequest,
      edu.eci.arsw.excercise8.ReserveResponse> getCancelReservationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelReservation",
      requestType = edu.eci.arsw.excercise8.CancelReserveRequest.class,
      responseType = edu.eci.arsw.excercise8.ReserveResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.CancelReserveRequest,
      edu.eci.arsw.excercise8.ReserveResponse> getCancelReservationMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.CancelReserveRequest, edu.eci.arsw.excercise8.ReserveResponse> getCancelReservationMethod;
    if ((getCancelReservationMethod = WorkshopServiceGrpc.getCancelReservationMethod) == null) {
      synchronized (WorkshopServiceGrpc.class) {
        if ((getCancelReservationMethod = WorkshopServiceGrpc.getCancelReservationMethod) == null) {
          WorkshopServiceGrpc.getCancelReservationMethod = getCancelReservationMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise8.CancelReserveRequest, edu.eci.arsw.excercise8.ReserveResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelReservation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.CancelReserveRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ReserveResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WorkshopServiceMethodDescriptorSupplier("CancelReservation"))
              .build();
        }
      }
    }
    return getCancelReservationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.AttendeeIdRequest,
      edu.eci.arsw.excercise8.ReservationList> getGetAttendeeReservationsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAttendeeReservations",
      requestType = edu.eci.arsw.excercise8.AttendeeIdRequest.class,
      responseType = edu.eci.arsw.excercise8.ReservationList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.AttendeeIdRequest,
      edu.eci.arsw.excercise8.ReservationList> getGetAttendeeReservationsMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.AttendeeIdRequest, edu.eci.arsw.excercise8.ReservationList> getGetAttendeeReservationsMethod;
    if ((getGetAttendeeReservationsMethod = WorkshopServiceGrpc.getGetAttendeeReservationsMethod) == null) {
      synchronized (WorkshopServiceGrpc.class) {
        if ((getGetAttendeeReservationsMethod = WorkshopServiceGrpc.getGetAttendeeReservationsMethod) == null) {
          WorkshopServiceGrpc.getGetAttendeeReservationsMethod = getGetAttendeeReservationsMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise8.AttendeeIdRequest, edu.eci.arsw.excercise8.ReservationList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAttendeeReservations"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.AttendeeIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ReservationList.getDefaultInstance()))
              .setSchemaDescriptor(new WorkshopServiceMethodDescriptorSupplier("GetAttendeeReservations"))
              .build();
        }
      }
    }
    return getGetAttendeeReservationsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest,
      edu.eci.arsw.excercise8.CapacityResponse> getGetAvailableSpotsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAvailableSpots",
      requestType = edu.eci.arsw.excercise8.ActivityRequest.class,
      responseType = edu.eci.arsw.excercise8.CapacityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest,
      edu.eci.arsw.excercise8.CapacityResponse> getGetAvailableSpotsMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise8.ActivityRequest, edu.eci.arsw.excercise8.CapacityResponse> getGetAvailableSpotsMethod;
    if ((getGetAvailableSpotsMethod = WorkshopServiceGrpc.getGetAvailableSpotsMethod) == null) {
      synchronized (WorkshopServiceGrpc.class) {
        if ((getGetAvailableSpotsMethod = WorkshopServiceGrpc.getGetAvailableSpotsMethod) == null) {
          WorkshopServiceGrpc.getGetAvailableSpotsMethod = getGetAvailableSpotsMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise8.ActivityRequest, edu.eci.arsw.excercise8.CapacityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAvailableSpots"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.ActivityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise8.CapacityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WorkshopServiceMethodDescriptorSupplier("GetAvailableSpots"))
              .build();
        }
      }
    }
    return getGetAvailableSpotsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WorkshopServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkshopServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkshopServiceStub>() {
        @java.lang.Override
        public WorkshopServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkshopServiceStub(channel, callOptions);
        }
      };
    return WorkshopServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WorkshopServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkshopServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkshopServiceBlockingStub>() {
        @java.lang.Override
        public WorkshopServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkshopServiceBlockingStub(channel, callOptions);
        }
      };
    return WorkshopServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WorkshopServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkshopServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkshopServiceFutureStub>() {
        @java.lang.Override
        public WorkshopServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkshopServiceFutureStub(channel, callOptions);
        }
      };
    return WorkshopServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void reserveSpot(edu.eci.arsw.excercise8.ReserveRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReserveResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReserveSpotMethod(), responseObserver);
    }

    /**
     */
    default void cancelReservation(edu.eci.arsw.excercise8.CancelReserveRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReserveResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelReservationMethod(), responseObserver);
    }

    /**
     */
    default void getAttendeeReservations(edu.eci.arsw.excercise8.AttendeeIdRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReservationList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAttendeeReservationsMethod(), responseObserver);
    }

    /**
     */
    default void getAvailableSpots(edu.eci.arsw.excercise8.ActivityRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.CapacityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAvailableSpotsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service WorkshopService.
   */
  public static abstract class WorkshopServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return WorkshopServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service WorkshopService.
   */
  public static final class WorkshopServiceStub
      extends io.grpc.stub.AbstractAsyncStub<WorkshopServiceStub> {
    private WorkshopServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkshopServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkshopServiceStub(channel, callOptions);
    }

    /**
     */
    public void reserveSpot(edu.eci.arsw.excercise8.ReserveRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReserveResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReserveSpotMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelReservation(edu.eci.arsw.excercise8.CancelReserveRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReserveResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelReservationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAttendeeReservations(edu.eci.arsw.excercise8.AttendeeIdRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReservationList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAttendeeReservationsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAvailableSpots(edu.eci.arsw.excercise8.ActivityRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.CapacityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAvailableSpotsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service WorkshopService.
   */
  public static final class WorkshopServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<WorkshopServiceBlockingStub> {
    private WorkshopServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkshopServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkshopServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.eci.arsw.excercise8.ReserveResponse reserveSpot(edu.eci.arsw.excercise8.ReserveRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReserveSpotMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise8.ReserveResponse cancelReservation(edu.eci.arsw.excercise8.CancelReserveRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelReservationMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise8.ReservationList getAttendeeReservations(edu.eci.arsw.excercise8.AttendeeIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAttendeeReservationsMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise8.CapacityResponse getAvailableSpots(edu.eci.arsw.excercise8.ActivityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAvailableSpotsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service WorkshopService.
   */
  public static final class WorkshopServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<WorkshopServiceFutureStub> {
    private WorkshopServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkshopServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkshopServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise8.ReserveResponse> reserveSpot(
        edu.eci.arsw.excercise8.ReserveRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReserveSpotMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise8.ReserveResponse> cancelReservation(
        edu.eci.arsw.excercise8.CancelReserveRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelReservationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise8.ReservationList> getAttendeeReservations(
        edu.eci.arsw.excercise8.AttendeeIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAttendeeReservationsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise8.CapacityResponse> getAvailableSpots(
        edu.eci.arsw.excercise8.ActivityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAvailableSpotsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RESERVE_SPOT = 0;
  private static final int METHODID_CANCEL_RESERVATION = 1;
  private static final int METHODID_GET_ATTENDEE_RESERVATIONS = 2;
  private static final int METHODID_GET_AVAILABLE_SPOTS = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RESERVE_SPOT:
          serviceImpl.reserveSpot((edu.eci.arsw.excercise8.ReserveRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReserveResponse>) responseObserver);
          break;
        case METHODID_CANCEL_RESERVATION:
          serviceImpl.cancelReservation((edu.eci.arsw.excercise8.CancelReserveRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReserveResponse>) responseObserver);
          break;
        case METHODID_GET_ATTENDEE_RESERVATIONS:
          serviceImpl.getAttendeeReservations((edu.eci.arsw.excercise8.AttendeeIdRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.ReservationList>) responseObserver);
          break;
        case METHODID_GET_AVAILABLE_SPOTS:
          serviceImpl.getAvailableSpots((edu.eci.arsw.excercise8.ActivityRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise8.CapacityResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getReserveSpotMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise8.ReserveRequest,
              edu.eci.arsw.excercise8.ReserveResponse>(
                service, METHODID_RESERVE_SPOT)))
        .addMethod(
          getCancelReservationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise8.CancelReserveRequest,
              edu.eci.arsw.excercise8.ReserveResponse>(
                service, METHODID_CANCEL_RESERVATION)))
        .addMethod(
          getGetAttendeeReservationsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise8.AttendeeIdRequest,
              edu.eci.arsw.excercise8.ReservationList>(
                service, METHODID_GET_ATTENDEE_RESERVATIONS)))
        .addMethod(
          getGetAvailableSpotsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise8.ActivityRequest,
              edu.eci.arsw.excercise8.CapacityResponse>(
                service, METHODID_GET_AVAILABLE_SPOTS)))
        .build();
  }

  private static abstract class WorkshopServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WorkshopServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.eci.arsw.excercise8.EcicienciaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WorkshopService");
    }
  }

  private static final class WorkshopServiceFileDescriptorSupplier
      extends WorkshopServiceBaseDescriptorSupplier {
    WorkshopServiceFileDescriptorSupplier() {}
  }

  private static final class WorkshopServiceMethodDescriptorSupplier
      extends WorkshopServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    WorkshopServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WorkshopServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WorkshopServiceFileDescriptorSupplier())
              .addMethod(getReserveSpotMethod())
              .addMethod(getCancelReservationMethod())
              .addMethod(getGetAttendeeReservationsMethod())
              .addMethod(getGetAvailableSpotsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
